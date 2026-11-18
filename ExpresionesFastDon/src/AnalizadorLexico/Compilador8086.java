package AnalizadorLexico;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Compilador8086 {
    private final List<String> tokens = new ArrayList<>();
    private final LinkedHashMap<String, String> variables = new LinkedHashMap<>();
    private final List<String> datos = new ArrayList<>();
    private final List<String> codigo = new ArrayList<>();
    private int pos = 0;
    private int etiqueta = 0;
    private String archivoSalida = "codigo_ensamblador.asm";

    public void compilar(String fuente, String salida) throws IOException {
        archivoSalida = salida;
        tokenizar(fuente);
        pos = 0;
        datos.clear();
        codigo.clear();
        variables.clear();
        etiqueta = 0;

        if (aceptar("main")) {
            aceptar("(");
            aceptar(")");
        }

        bloque();

        if (pos < tokens.size()) {
            error("Contenido no reconocido: " + actual());
        }

        escribirArchivo();
    }

    private void tokenizar(String fuente) {
        tokens.clear();

        Pattern patron = Pattern.compile(
            "//[^\\n]*|/\\*[\\s\\S]*?\\*/|\\\"(?:\\\\.|[^\\\"])*\\\"|" +
            "(?:[A-Za-z_][A-Za-z_0-9]*)|(?:\\d+)|" +
            "(?:\\*\\*|\\+\\+|--|==|!=|<=|>=|&&|\\|\\||[{}();,:=+*/%<>!-])"
        );

        Matcher m = patron.matcher(fuente);
        int finAnterior = 0;

        while (m.find()) {
            String espacio = fuente.substring(finAnterior, m.start());

            if (!espacio.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Caracter no reconocido: " + espacio.trim()
                );
            }

            String t = m.group();

            if (!t.startsWith("//") && !t.startsWith("/*")) {
                tokens.add(t);
            }

            finAnterior = m.end();
        }

        if (!fuente.substring(finAnterior).trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Caracter no reconocido: " +
                fuente.substring(finAnterior).trim()
            );
        }
    }

    private String actual() {
        return pos < tokens.size() ? tokens.get(pos) : "<fin>";
    }

    private String ver(int n) {
        return pos + n < tokens.size() ? tokens.get(pos + n) : "<fin>";
    }

    private boolean aceptar(String t) {
        if (actual().equals(t)) {
            pos++;
            return true;
        }
        return false;
    }

    private void exigir(String t) {
        if (!aceptar(t)) {
            error("Se esperaba '" + t + "' y se encontro '" + actual() + "'");
        }
    }

    private void error(String m) {
        throw new IllegalArgumentException(
            "Error en token " + (pos + 1) + ": " + m
        );
    }

    private String nuevaEtiqueta(String base) {
        return base + etiqueta++;
    }

    private void emitir(String s) {
        codigo.add("    " + s);
    }

    private void bloque() {
        exigir("{");

        while (!actual().equals("}") && !actual().equals("<fin>")) {
            sentencia();
        }

        exigir("}");
    }

    private void sentencia() {
        switch (actual()) {
            case "int":
            case "boolean":
            case "String":
                declaracion();
                break;

            case "if":
                condicional();
                break;

            case "while":
                cicloWhile();
                break;

            case "for":
                cicloFor();
                break;

            case "switch":
                estructuraSwitch();
                break;

            case "print":
                imprimir();
                break;

            case "inputI":
            case "inputB":
            case "inputS":
                entrada();
                break;

            case "{":
                bloque();
                break;

            case ";":
                pos++;
                break;

            case "break":
                pos++;
                exigir(";");
                emitir("jmp " + (etiquetaBreak.isEmpty()
                    ? "fin_programa" : etiquetaBreak.peek()));
                break;

            default:
                asignacion();
        }
    }

    private final Deque<String> etiquetaBreak = new ArrayDeque<>();

    private void declarar(String nombre, String tipo) {
        if (variables.containsKey(nombre)) {
            if (!variables.get(nombre).equals(tipo)) {
                error("La variable ya existe con otro tipo: " + nombre);
            }
            return;
        }

        variables.put(nombre, tipo);

        if (tipo.equals("int") || tipo.equals("boolean")) {
            datos.add(nombre + " dw 0");
        } else {
            datos.add(nombre + " db 255,0,256 dup('$')");
        }
    }

    private void declaracion() {
        String tipo = actual();
        pos++;

        do {
            String nombre = actual();

            if (!nombre.matches("[A-Za-z_][A-Za-z_0-9]*")) {
                error("Nombre de variable no valido");
            }

            pos++;
            declarar(nombre, tipo);

            if (aceptar("=")) {
                if (tipo.equals("String")) {
                    if (!actual().startsWith("\"")) {
                        error("String requiere un texto entre comillas");
                    }

                    String literal = cadena(actual());
                    pos++;

                    datos.add(
                        nombre + "_texto db '" + asmTexto(literal) + "','$'"
                    );

                    emitir("lea dx, " + nombre + "_texto");
                    emitir("lea di, " + nombre + "+2");
                    emitir("mov si, dx");
                    emitir("call copiar_cadena");

                } else {
                    expresion();
                    emitir("mov " + nombre + ", ax");
                }
            }

        } while (aceptar(","));

        exigir(";");
    }

    private void asignacion() {
        String nombre = actual();

        if (!variables.containsKey(nombre)) {
            error("Variable no declarada: " + nombre);
        }

        pos++;

        if (aceptar("++")) {
            emitir("inc " + nombre);
            exigir(";");
            return;
        }

        if (aceptar("--")) {
            emitir("dec " + nombre);
            exigir(";");
            return;
        }

        exigir("=");

        if (variables.get(nombre).equals("String")) {
            if (!actual().startsWith("\"")) {
                error("Asignacion String: se esperaba texto literal");
            }

            String texto = cadena(actual());
            pos++;

            String etiquetaTexto = nuevaEtiqueta("cadena");

            datos.add(
                etiquetaTexto + " db '" + asmTexto(texto) + "','$'"
            );

            emitir("lea si, " + etiquetaTexto);
            emitir("lea di, " + nombre + "+2");
            emitir("call copiar_cadena");

        } else {
            expresion();
            emitir("mov " + nombre + ", ax");
        }

        exigir(";");
    }

    private void condicional() {
        pos++;
        exigir("(");
        expresion();
        exigir(")");

        String sino = nuevaEtiqueta("sino");
        String fin = nuevaEtiqueta("fin_si");

        emitir("cmp ax, 0");
        emitir("je " + sino);

        bloque();

        if (aceptar("else")) {
            emitir("jmp " + fin);
            codigo.add(sino + ":");
            bloque();
            codigo.add(fin + ":");
        } else {
            codigo.add(sino + ":");
        }
    }

    private void cicloWhile() {
        pos++;

        String inicio = nuevaEtiqueta("mientras");
        String fin = nuevaEtiqueta("fin_mientras");

        codigo.add(inicio + ":");

        exigir("(");
        expresion();
        exigir(")");

        emitir("cmp ax, 0");
        emitir("je " + fin);

        etiquetaBreak.push(fin);
        bloque();
        etiquetaBreak.pop();

        emitir("jmp " + inicio);
        codigo.add(fin + ":");
    }

    private void cicloFor() {
        pos++;
        exigir("(");

        if (actual().equals("int") || actual().equals("boolean")) {
            String tipo = actual();
            pos++;

            String nombre = actual();
            pos++;

            declarar(nombre, tipo);

            if (aceptar("=")) {
                expresion();
                emitir("mov " + nombre + ", ax");
            }

        } else if (!actual().equals(";")) {
            asignacionSinPuntoComa();
        }

        exigir(";");

        String inicio = nuevaEtiqueta("para");
        String fin = nuevaEtiqueta("fin_para");

        codigo.add(inicio + ":");

        if (!actual().equals(";")) {
            expresion();
            emitir("cmp ax, 0");
            emitir("je " + fin);
        }

        exigir(";");

        String incremento = nuevaEtiqueta("incremento");
        List<String> guardado = new ArrayList<>();

        while (!actual().equals(")") && !actual().equals("<fin>")) {
            String nombre = actual();
            pos++;

            if (!variables.containsKey(nombre)) {
                error("Variable no declarada: " + nombre);
            }

            if (aceptar("++")) {
                guardado.add("inc " + nombre);
            } else if (aceptar("--")) {
                guardado.add("dec " + nombre);
            } else if (aceptar("=")) {
                expresion();
                guardado.add("mov " + nombre + ", ax");
            } else {
                error("Incremento de for no valido");
            }

            if (!aceptar(",")) {
                break;
            }
        }

        exigir(")");

        etiquetaBreak.push(fin);
        bloque();
        etiquetaBreak.pop();

        codigo.add(incremento + ":");

        for (String s : guardado) {
            emitir(s);
        }

        emitir("jmp " + inicio);
        codigo.add(fin + ":");
    }

    private void asignacionSinPuntoComa() {
        String nombre = actual();
        pos++;

        exigir("=");

        expresion();
        emitir("mov " + nombre + ", ax");
    }

    private void estructuraSwitch() {
        pos++;
        exigir("(");
        expresion();
        exigir(")");
        exigir("{");

        String fin = nuevaEtiqueta("fin_switch");
        etiquetaBreak.push(fin);

        while (actual().equals("case")) {
            pos++;

            String valor = actual();
            pos++;

            if (!valor.matches("-?\\d+|true|false")) {
                error("Case requiere literal entero o booleano");
            }

            exigir(":");

            String siguiente = nuevaEtiqueta("case_siguiente");

            emitir("cmp ax, " +
                (valor.equals("true") ? "1" :
                valor.equals("false") ? "0" : valor));

            emitir("jne " + siguiente);

            while (!actual().equals("case")
                    && !actual().equals("default")
                    && !actual().equals("}")) {
                sentencia();
            }

            emitir("jmp " + fin);
            codigo.add(siguiente + ":");
        }

        if (aceptar("default")) {
            exigir(":");

            while (!actual().equals("}")) {
                sentencia();
            }
        }

        exigir("}");
        etiquetaBreak.pop();

        codigo.add(fin + ":");
    }

    private void imprimir() {
        pos++;
        exigir("(");

        boolean primero = true;

        while (!actual().equals(")")) {
            if (!primero) {
                exigir("++");
            }

            primero = false;

            if (actual().startsWith("\"")) {
                String texto = cadena(actual());
                pos++;

                String et = nuevaEtiqueta("salida");

                datos.add(et + " db '" + asmTexto(texto) + "','$'");

                emitir("lea dx, " + et);
                emitir("mov ah, 09h");
                emitir("int 21h");

            } else {
                String nombre = actual();

                if (variables.containsKey(nombre)
                        && variables.get(nombre).equals("String")) {
                    pos++;
                    emitir("lea dx, " + nombre + "+2");
                    emitir("call imprimir_cadena");
                } else {
                    expresionHastaConcat();
                    emitir("call imprimir_entero");
                }
            }
        }

        exigir(")");
        exigir(";");
    }

    private void expresionHastaConcat() {
        expresion();
    }

    private void entrada() {
        String funcion = actual();
        pos++;

        exigir("(");

        String nombre = actual();
        pos++;

        exigir(")");
        exigir(";");

        if (!variables.containsKey(nombre)) {
            error("Variable no declarada: " + nombre);
        }

        if (funcion.equals("inputI")) {
            emitir("call leer_entero");
            emitir("mov " + nombre + ", ax");

        } else if (funcion.equals("inputB")) {
            emitir("call leer_entero");
            emitir("mov " + nombre + ", ax");

        } else {
            emitir("lea dx, " + nombre);
            emitir("mov ah, 0Ah");
            emitir("int 21h");
            emitir("call terminar_cadena");
        }
    }

    private void expresion() {
        logicoOr();
    }

    private void logicoOr() {
        logicoAnd();

        while (aceptar("||")) {
            emitir("push ax");
            logicoAnd();
            emitir("mov bx, ax");
            emitir("pop ax");
            emitir("or ax, bx");
            normalizarBooleano();
        }
    }

    private void logicoAnd() {
        igualdad();

        while (aceptar("&&")) {
            emitir("push ax");
            igualdad();
            emitir("mov bx, ax");
            emitir("pop ax");
            emitir("and ax, bx");
            normalizarBooleano();
        }
    }

    private void igualdad() {
        comparacion();

        while (actual().equals("==") || actual().equals("!=")) {
            String op = actual();
            pos++;

            emitir("push ax");
            comparacion();
            emitir("mov bx, ax");
            emitir("pop ax");

            comparar(op);
        }
    }

    private void comparacion() {
        suma();

        while (Arrays.asList(">", "<", ">=", "<=").contains(actual())) {
            String op = actual();
            pos++;

            emitir("push ax");
            suma();
            emitir("mov bx, ax");
            emitir("pop ax");

            comparar(op);
        }
    }

    private void suma() {
        producto();

        while (actual().equals("+") || actual().equals("-")) {
            String op = actual();
            pos++;

            emitir("push ax");
            producto();
            emitir("mov bx, ax");
            emitir("pop ax");

            emitir(op.equals("+") ? "add ax, bx" : "sub ax, bx");
        }
    }

    private void producto() {
        potencia();

        while (Arrays.asList("*", "/", "%").contains(actual())) {
            String op = actual();
            pos++;

            emitir("push ax");
            potencia();
            emitir("mov bx, ax");
            emitir("pop ax");

            if (op.equals("*")) {
                emitir("imul bx");
            } else {
                emitir("cwd");
                emitir("idiv bx");

                if (op.equals("%")) {
                    emitir("mov ax, dx");
                }
            }
        }
    }

    private void potencia() {
        primario();

        if (aceptar("**")) {
            emitir("push ax");
            potencia();
            emitir("mov cx, ax");
            emitir("pop bx");
            emitir("mov ax, 1");

            String inicio = nuevaEtiqueta("potencia");
            String fin = nuevaEtiqueta("fin_potencia");

            codigo.add(inicio + ":");
            emitir("cmp cx, 0");
            emitir("je " + fin);
            emitir("imul bx");
            emitir("dec cx");
            emitir("jmp " + inicio);
            codigo.add(fin + ":");
        }
    }

    private void primario() {
        if (aceptar("-")) {
            primario();
            emitir("neg ax");
            return;
        }

        if (aceptar("!")) {
            primario();

            emitir("cmp ax, 0");

            String falso = nuevaEtiqueta("not0");
            String fin = nuevaEtiqueta("notfin");

            emitir("je " + falso);
            emitir("mov ax, 0");
            emitir("jmp " + fin);

            codigo.add(falso + ":");
            emitir("mov ax, 1");

            codigo.add(fin + ":");
            return;
        }

        if (aceptar("(")) {
            expresion();
            exigir(")");
            return;
        }

        String t = actual();
        pos++;

        if (t.equals("true")) {
            emitir("mov ax, 1");
        } else if (t.equals("false")) {
            emitir("mov ax, 0");
        } else if (t.matches("\\d+")) {
            emitir("mov ax, " + t);
        } else if (variables.containsKey(t)) {
            if (variables.get(t).equals("String")) {
                error("No se puede usar String en una operacion numerica");
            }

            emitir("mov ax, " + t);
        } else {
            error("Valor o variable no reconocido: " + t);
        }
    }

    private void comparar(String op) {
        emitir("cmp ax, bx");

        String verdadero = nuevaEtiqueta("verdadero");
        String fin = nuevaEtiqueta("comparacion_fin");

        String salto;

        switch (op) {
            case "==": salto = "je"; break;
            case "!=": salto = "jne"; break;
            case ">": salto = "jg"; break;
            case "<": salto = "jl"; break;
            case ">=": salto = "jge"; break;
            case "<=": salto = "jle"; break;
            default:
                throw new IllegalArgumentException("Operador no valido: " + op);
        }

        emitir(salto + " " + verdadero);
        emitir("mov ax, 0");
        emitir("jmp " + fin);

        codigo.add(verdadero + ":");
        emitir("mov ax, 1");

        codigo.add(fin + ":");
    }

    private void normalizarBooleano() {
        emitir("cmp ax, 0");

        String v = nuevaEtiqueta("bool_v");
        String f = nuevaEtiqueta("bool_f");

        emitir("jne " + v);
        emitir("mov ax, 0");
        emitir("jmp " + f);

        codigo.add(v + ":");
        emitir("mov ax, 1");

        codigo.add(f + ":");
    }

    private String cadena(String token) {
        return token.substring(1, token.length() - 1)
            .replace("\\\"", "\"")
            .replace("\\n", " ");
    }

    private String asmTexto(String s) {
        return s.replace("'", "''")
            .replace("\r", " ")
            .replace("\n", " ");
    }

    private void escribirArchivo() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(archivoSalida))) {
            w.write(".MODEL SMALL\n.STACK 100h\n.DATA\n");

            for (String d : datos) {
                w.write("    " + d + "\n");
            }

            w.write(
                "    numero dw 0\n" +
                "    digitos db 6 dup('$')\n" +
                ".CODE\n" +
                "main PROC\n" +
                "    mov ax, @data\n" +
                "    mov ds, ax\n" +
                "    mov es, ax\n"
            );

            for (String s : codigo) {
                w.write(s + "\n");
            }

            w.write(
                "fin_programa:\n" +
                "    mov ax, 4C00h\n" +
                "    int 21h\n" +
                "main ENDP\n\n" +
                rutinas() +
                "\nEND main\n"
            );
        }
    }

    private String rutinas() {
        return "imprimir_entero PROC\n" +
            "    push ax\n" +
            "    push bx\n" +
            "    push cx\n" +
            "    push dx\n" +
            "    mov bx, 10\n" +
            "    xor cx, cx\n" +
            "    cmp ax, 0\n" +
            "    jge conv_pos\n" +
            "    push ax\n" +
            "    mov dl, '-'\n" +
            "    mov ah, 02h\n" +
            "    int 21h\n" +
            "    pop ax\n" +
            "    neg ax\n" +
            "conv_pos:\n" +
            "    xor dx, dx\n" +
            "conv_loop:\n" +
            "    div bx\n" +
            "    push dx\n" +
            "    inc cx\n" +
            "    xor dx, dx\n" +
            "    cmp ax, 0\n" +
            "    jne conv_loop\n" +
            "conv_print:\n" +
            "    pop dx\n" +
            "    add dl, '0'\n" +
            "    mov ah, 02h\n" +
            "    int 21h\n" +
            "    loop conv_print\n" +
            "    pop dx\n" +
            "    pop cx\n" +
            "    pop bx\n" +
            "    pop ax\n" +
            "    ret\n" +
            "imprimir_entero ENDP\n" +

            "leer_entero PROC\n" +
            "    push bx\n" +
            "    push cx\n" +
            "    push dx\n" +
            "    xor bx, bx\n" +
            "leer_digito:\n" +
            "    mov ah, 01h\n" +
            "    int 21h\n" +
            "    cmp al, 13\n" +
            "    je leer_fin\n" +
            "    cmp al, '0'\n" +
            "    jb leer_digito\n" +
            "    cmp al, '9'\n" +
            "    ja leer_digito\n" +
            "    sub al, '0'\n" +
            "    xor ah, ah\n" +
            "    mov cx, ax\n" +
            "    mov ax, bx\n" +
            "    mov dx, 10\n" +
            "    mul dx\n" +
            "    add ax, cx\n" +
            "    mov bx, ax\n" +
            "    jmp leer_digito\n" +
            "leer_fin:\n" +
            "    mov ax, bx\n" +
            "    pop dx\n" +
            "    pop cx\n" +
            "    pop bx\n" +
            "    ret\n" +
            "leer_entero ENDP\n" +

            "imprimir_cadena PROC\n" +
            "    push ax\n" +
            "    push dx\n" +
            "    mov ah, 09h\n" +
            "    int 21h\n" +
            "    pop dx\n" +
            "    pop ax\n" +
            "    ret\n" +
            "imprimir_cadena ENDP\n" +

            "copiar_cadena PROC\n" +
            "    push ax\n" +
            "    push si\n" +
            "    push di\n" +
            "copiar_loop:\n" +
            "    lodsb\n" +
            "    cmp al, '$'\n" +
            "    je copiar_fin\n" +
            "    stosb\n" +
            "    jmp copiar_loop\n" +
            "copiar_fin:\n" +
            "    mov al, '$'\n" +
            "    stosb\n" +
            "    pop di\n" +
            "    pop si\n" +
            "    pop ax\n" +
            "    ret\n" +
            "copiar_cadena ENDP\n" +

            "terminar_cadena PROC\n" +
            "    push ax\n" +
            "    push bx\n" +
            "    push di\n" +
            "    push si\n" +
            "    mov si, dx\n" +
            "    xor bx, bx\n" +
            "    mov bl, [si+1]\n" +
            "    lea di, [si+2]\n" +
            "    add di, bx\n" +
            "    mov byte ptr [di], '$'\n" +
            "    pop si\n" +
            "    pop di\n" +
            "    pop bx\n" +
            "    pop ax\n" +
            "    ret\n" +
            "terminar_cadena ENDP\n";
    }

    public static void main(String[] args) {
        try {
            Path carpeta = buscarCarpetaEjercicios();

            if (carpeta == null) {
                System.out.println("No se encontraron los cuatro ejercicios .txt.");
                return;
            }

            for (int i = 1; i <= 4; i++) {
                String nombre = "ejercicio" + i;
                Path entrada = carpeta.resolve(nombre + ".txt");
                Path salida = carpeta.resolve(nombre + ".asm");

                String fuente = Files.readString(entrada);
                Compilador8086 compilador = new Compilador8086();
                compilador.compilar(fuente, salida.toString());

                System.out.println("Generado: " + salida.toAbsolutePath());
            }

            System.out.println("Traduccion de los cuatro ejercicios terminada.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Path buscarCarpetaEjercicios() {
        Path ruta = Paths.get("").toAbsolutePath();

        while (ruta != null) {
            boolean encontrados = true;

            for (int i = 1; i <= 4; i++) {
                Path archivo = ruta.resolve("ejercicio" + i + ".txt");

                if (!Files.exists(archivo)) {
                    encontrados = false;
                    break;
                }
            }

            if (encontrados) {
                return ruta;
            }

            ruta = ruta.getParent();
        }

        return null;
    }
}
