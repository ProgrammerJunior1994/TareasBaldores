package AnalizadorLexico;

import static AnalizadorLexico.analizadorPartTwo.reservadas;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Analizador semántico con generación de código intermedio (triplos)
 * y código ensamblador usando una pila ensambladora (pila_em).
 *
 * Reglas de la pila_em:
 *  - Inicia con "_" (igual que la pila semántica).
 *  - Desplazamiento de id/variable -> push(idxx)  (se usa el id interno).
 *  - Desplazamiento de operador/paréntesis -> push(símbolo).
 *  - Reducción de operación -> pop operandos+operador, push(tN) y triplo.
 *  - Reducción unitaria -> sin cambio.
 *  - Reducción de paréntesis -> quita ( y ), conserva el valor.
 *
 * @author Jaime Ramirez
 */
public class AnalizadorSemantico {

    public static Scanner sc = new Scanner(System.in);

    // Pila de estados
    private Stack<Integer> pilaEstados = new Stack<>();
    // Pila semántica
    private Stack<String> pilaSem = new Stack<>();
    // Pila ensambladora (operandos / temporales / operadores)
    private Stack<String> pilaEm = new Stack<>();
    // Contador de temporales
    private int contadorTemporales = 0;
    // Lista de triplos generados: (op, op1, op2, resultado)
    private List<String[]> triplos = new ArrayList<>();
    // Código ensamblador generado a partir de los triplos
    private List<String> codigoEnsamblador = new ArrayList<>();

    private Map<Integer, Map<String, String>> tabla = new HashMap<>();
    private Map<String, String> tablaTipos = new HashMap<>();      // idxx -> tipo
    private Map<String, String> nombresAId = new HashMap<>();      // variable -> idxx
    private Map<String, String> literalesAId = new HashMap<>();    // literal  -> idxx
    private Map<String, String> idAOriginal = new HashMap<>();     // idxx -> nombre legible

    private List<String> tiposEntrada = new ArrayList<>();
    private List<String> lexemasEntrada = new ArrayList<>();

    // ---------------------------------------------------------------
    // Registro de variables
    // ---------------------------------------------------------------
    public void registrarVariable(String nombre, String tipo) {
        String id = "id" + tablaTipos.size();
        tablaTipos.put(id, tipo);
        nombresAId.put(nombre, id);
        idAOriginal.put(id, nombre);
        System.out.println("Variable " + nombre + " registrada como " + id + " de tipo " + tipo);
    }

    /**
     * Devuelve el idxx asociado a un literal (3, true, false, ...).
     * Si no existe, lo crea dinámicamente.
     */
    private String idDeLiteral(String literal) {
        String existente = literalesAId.get(literal);
        if (existente != null) {
            return existente;
        }
        String id = "id" + tablaTipos.size();
        String tipo = "int";
        if (literal.equals("true") || literal.equals("false")) {
            tipo = "boolean";
        }
        tablaTipos.put(id, tipo);
        literalesAId.put(literal, id);
        idAOriginal.put(id, literal);
        return id;
    }

    /**
     * Obtiene el idxx interno para un lexema (variable o literal).
     */
    private String idInternoDeLexema(String lexema) {
        if (nombresAId.containsKey(lexema)) {
            return nombresAId.get(lexema);
        }
        return idDeLiteral(lexema);
    }

    // ---------------------------------------------------------------
    // Tokenización
    // ---------------------------------------------------------------
    public List<String> tokenizarEntrada(String expresion) {
        List<String> tokens = new ArrayList<>();
        tiposEntrada.clear();
        lexemasEntrada.clear();
        int posicion = 0;
        while (posicion < expresion.length()) {
            char actual = expresion.charAt(posicion);
            if (Character.isWhitespace(actual)) {
                posicion++;
                continue;
            }

            String operador = operadorEn(expresion, posicion);
            if (operador != null) {
                tokens.add(operador);
                tiposEntrada.add(operador);
                lexemasEntrada.add(operador);
                posicion += operador.length();
                continue;
            }

            if (actual == '$') {
                tokens.add("$");
                tiposEntrada.add("$");
                lexemasEntrada.add("$");
                posicion++;
                continue;
            }

            if (Character.isLetter(actual) || actual == '_') {
                int inicio = posicion++;
                while (posicion < expresion.length()
                        && (Character.isLetterOrDigit(expresion.charAt(posicion))
                        || expresion.charAt(posicion) == '_')) {
                    posicion++;
                }
                String lexema = expresion.substring(inicio, posicion);
                if (lexema.equals("true") || lexema.equals("false")) {
                    tablaTipos.putIfAbsent(lexema, "boolean");
                    tokens.add("id");
                    tiposEntrada.add("boolean");
                    lexemasEntrada.add(lexema);
                } else if (nombresAId.containsKey(lexema)) {
                    tokens.add("id");
                    tiposEntrada.add(tablaTipos.get(nombresAId.get(lexema)));
                    lexemasEntrada.add(lexema);
                } else {
                    throw new IllegalArgumentException("Variable no registrada: " + lexema);
                }
                continue;
            }

            if (Character.isDigit(actual)) {
                int inicio = posicion++;
                while (posicion < expresion.length() && Character.isDigit(expresion.charAt(posicion))) {
                    posicion++;
                }
                String literal = expresion.substring(inicio, posicion);
                tablaTipos.putIfAbsent(literal, "int");
                tokens.add("id");
                tiposEntrada.add("int");
                lexemasEntrada.add(literal);
                continue;
            }

            throw new IllegalArgumentException("Simbolo no reconocido en la posicion " + posicion + ": " + actual);
        }
        if (tokens.isEmpty() || !tokens.get(tokens.size() - 1).equals("$")) {
            tokens.add("$");
            tiposEntrada.add("$");
            lexemasEntrada.add("$");
        }
        return tokens;
    }

    private String operadorEn(String expresion, int posicion) {
        String[] operadores = {"||", "&&", "==", "!=", ">=", "<=", "**", ">", "<", "+", "-", "*", "/", "%", "!", "(", ")"};
        for (String operador : operadores) {
            if (expresion.startsWith(operador, posicion)) {
                return operador;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Utilidades de pila semántica / ensambladora (sin '|')
    // ---------------------------------------------------------------
    private String tipoDeValor(String valor) {
        int separador = valor.indexOf(':');
        return separador >= 0 ? valor.substring(separador + 1) : valor;
    }

    private String valorTipado(String simbolo, String tipo) {
        return simbolo + ":" + tipo;
    }

    private String pilaSemanticaComoTexto() {
        return String.join(" ", pilaSem);
    }

    private String pilaEnsambladoraComoTexto() {
        return String.join(" ", pilaEm);
    }

    private String entradaPendiente(List<String> entrada, int posicion) {
        if (posicion >= entrada.size()) {
            return "<vacia>";
        }
        return String.join(" ", entrada.subList(posicion, entrada.size()));
    }

    private String celda(String valor, int ancho) {
        if (valor == null) {
            valor = "";
        }
        if (valor.length() > ancho) {
            return valor.substring(0, ancho - 3) + "...";
        }
        return String.format("%-" + ancho + "s", valor);
    }

    private void imprimirFila(String paso, String entrada, String accion,
            String estados, String pilaSem, String pilaEm) {
        System.out.printf("| %s | %s | %s | %s | %s | %s |%n",
                celda(paso, 6), celda(entrada, 34), celda(accion, 48),
                celda(estados, 28), celda(pilaSem, 50), celda(pilaEm, 50));
    }

    private void imprimirSeparador() {
        System.out.println("+--------+------------------------------------+--------------------------------------------------+------------------------------+"
                + "----------------------------------------------------+----------------------------------------------------+");
    }

    private void mostrarTokens(List<String> entrada) {
        StringBuilder salida = new StringBuilder();
        for (int posicion = 0; posicion < entrada.size(); posicion++) {
            String simbolo = entrada.get(posicion);
            String tipo = posicion < tiposEntrada.size() ? tiposEntrada.get(posicion) : simbolo;
            if (simbolo.equals("id")) {
                salida.append(String.format("[%d] id<%s>  ", posicion + 1, tipo));
            } else {
                salida.append(String.format("[%d] %s  ", posicion + 1, simbolo));
            }
        }
        System.out.println("Tokens: " + salida.toString().trim());
    }

    // ---------------------------------------------------------------
    // Validación semántica de tipos
    // ---------------------------------------------------------------
    private String tipoDeReduccion(int numero, List<String> valores) {
        String izquierdo;
        switch (numero) {
            case 1:
            case 3:
                exigirTipo(valores, "boolean", numero);
                izquierdo = "boolean";
                break;
            case 2:
            case 4:
            case 11:
            case 14:
            case 18:
            case 20:
                izquierdo = tipoDeValor(valores.get(0));
                break;
            case 5:
            case 6:
                if (!tipoDeValor(valores.get(0)).equals(tipoDeValor(valores.get(2)))) {
                    throw new IllegalArgumentException("Los operandos de la comparacion deben tener el mismo tipo");
                }
                izquierdo = "boolean";
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                exigirTipo(valores, "int", numero);
                izquierdo = "boolean";
                break;
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 19:
                exigirTipo(valores, "int", numero);
                izquierdo = "int";
                break;
            case 21:
                exigirTipo(valores, "boolean", numero);
                izquierdo = "boolean";
                break;
            case 22:
                izquierdo = tipoDeValor(valores.get(1));
                break;
            case 23:
                izquierdo = tipoDeValor(valores.get(0));
                break;
            default:
                throw new IllegalArgumentException("Produccion no definida: " + numero);
        }
        return izquierdo;
    }

    private void exigirTipo(List<String> valores, String esperado, int produccion) {
        for (String valor : valores) {
            String tipo = tipoDeValor(valor);
            if (!tipo.equals(esperado) && !valor.startsWith("||:") && !valor.startsWith("&&:")
                    && !valor.startsWith("==:") && !valor.startsWith("!=:")
                    && !valor.startsWith(">:") && !valor.startsWith("<:")
                    && !valor.startsWith(">=:") && !valor.startsWith("<=:")
                    && !valor.startsWith("+:") && !valor.startsWith("-:")
                    && !valor.startsWith("*:") && !valor.startsWith("/:")
                    && !valor.startsWith("%:") && !valor.startsWith("**:")
                    && !valor.startsWith("!:") && !valor.startsWith("(:")
                    && !valor.startsWith("):")) {
                throw new IllegalArgumentException("La produccion r" + produccion
                        + " requiere operandos " + esperado + " y recibio " + tipo);
            }
        }
    }

    // ---------------------------------------------------------------
    // Pila ensambladora: manipulación según el tipo de reducción
    // ---------------------------------------------------------------
    private boolean esOperacion(int produccion) {
        switch (produccion) {
            case 1: case 3: case 5: case 6: case 7: case 8: case 9: case 10:
            case 12: case 13: case 15: case 16: case 17: case 19: case 21:
                return true;
            default:
                return false;
        }
    }

    private String operadorDeProduccion(int produccion) {
        switch (produccion) {
            case 1:  return "||";
            case 3:  return "&&";
            case 5:  return "==";
            case 6:  return "!=";
            case 7:  return ">";
            case 8:  return "<";
            case 9:  return ">=";
            case 10: return "<=";
            case 12: return "+";
            case 13: return "-";
            case 15: return "*";
            case 16: return "/";
            case 17: return "%";
            case 19: return "**";
            case 21: return "!";
            default: return null;
        }
    }

    private String instruccionEnsamblador(int produccion) {
        switch (produccion) {
            case 1: return "OR";
            case 3: return "AND";
            case 5: return "EQ";
            case 6: return "NE";
            case 7: return "GT";
            case 8: return "LT";
            case 9: return "GE";
            case 10: return "LE";
            case 12: return "ADD";
            case 13: return "SUB";
            case 15: return "MUL";
            case 16: return "DIV";
            case 17: return "MOD";
            case 19: return "POW";
            case 21: return "NOT";
            default: return null;
        }
    }

    private String nuevoTemporal() {
        contadorTemporales++;
        return "t" + contadorTemporales;
    }

    /**
     * Actualiza la pila_em al reducir una producción.
     *
     * @param produccion número de producción
     * @param idInterno  idxx del id desplazado (solo se usa en r23)
     * @return descripción de la acción realizada en la pila_em
     */
    private String actualizarPilaEm(int produccion, String idInterno) {
        // F -> id  (r23)
        if (produccion == 23) {
            pilaEm.push(idInterno);
            return "push(" + idInterno + ")";
        }

        // F -> ( A )  (r22)
        if (produccion == 22) {
            pilaEm.pop();                 // ')'
            String valor = pilaEm.pop();  // A (ya es idxx o temporal)
            pilaEm.pop();                 // '('
            pilaEm.push(valor);
            return "quita ( ), conserva " + valor;
        }

        // Operaciones reales
        if (esOperacion(produccion)) {
            String op = operadorDeProduccion(produccion);
            String temp = nuevoTemporal();

            if (produccion == 21) { // !F  -> unario
                String op1 = pilaEm.pop(); // F
                pilaEm.pop();              // '!'
                pilaEm.push(temp);
                triplos.add(new String[]{op, op1, "-", temp});
                codigoEnsamblador.add("LOAD " + nombreLegible(op1));
                codigoEnsamblador.add("NOT");
                codigoEnsamblador.add("STORE " + temp);
                return "triplo (" + op + ", " + op1 + ", -, " + temp + ") push(" + temp + ")";
            }

            // Binarias: op1 op op2
            String op2 = pilaEm.pop();
            pilaEm.pop();               // operador
            String op1 = pilaEm.pop();
            pilaEm.push(temp);

            triplos.add(new String[]{op, op1, op2, temp});
            codigoEnsamblador.add("LOAD " + nombreLegible(op1));
            codigoEnsamblador.add(instruccionEnsamblador(produccion) + " " + nombreLegible(op2));
            codigoEnsamblador.add("STORE " + temp);
            return "triplo (" + op + ", " + op1 + ", " + op2 + ", " + temp + ") push(" + temp + ")";
        }

        // Reducciones unitarias: A->B, B->R, R->C, C->D, D->E, E->F
        return "unitaria (pila_em sin cambios)";
    }

    /**
     * Traduce un idxx a su nombre legible para el ensamblador.
     * Si no existe (por ejemplo un temporal), se deja igual.
     */
    private String nombreLegible(String idxx) {
        return idAOriginal.getOrDefault(idxx, idxx);
    }

    // ---------------------------------------------------------------
    // Carga de tabla LR
    // ---------------------------------------------------------------
    public void cargarTabla(String archivoCSV, List<String> simbolos) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            int estado = 0;
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(",");
                Map<String, String> acciones = new HashMap<>();
                for (int j = 0; j < columnas.length; j++) {
                    if (!columnas[j].isEmpty()) {
                        acciones.put(simbolos.get(j), columnas[j]);
                    }
                }
                tabla.put(estado++, acciones);
            }
        }
        System.out.println("Tabla LR cargada con " + tabla.size() + " estados.");
    }

    // ---------------------------------------------------------------
    // Parser LR con generación de triplos y ensamblador
    // ---------------------------------------------------------------
    public void parse(List<String> entrada) throws IOException {
        Files.deleteIfExists(new File("codigo_ensamblador.asm").toPath());
        Files.deleteIfExists(new File("codigo_intermedio.txt").toPath());

        pilaEstados.clear();
        pilaSem.clear();
        pilaEm.clear();
        triplos.clear();
        codigoEnsamblador.clear();
        contadorTemporales = 0;

        pilaEstados.push(0);
        pilaSem.push("_");
        pilaEm.push("_");   // <-- pila_em inicia con _

        Map<Integer, Produccion> producciones = new HashMap<>();
        producciones.put(1,  new Produccion("A", Arrays.asList("A", "||", "B")));
        producciones.put(2,  new Produccion("A", Arrays.asList("B")));
        producciones.put(3,  new Produccion("B", Arrays.asList("B", "&&", "R")));
        producciones.put(4,  new Produccion("B", Arrays.asList("R")));
        producciones.put(5,  new Produccion("R", Arrays.asList("R", "==", "C")));
        producciones.put(6,  new Produccion("R", Arrays.asList("R", "!=", "C")));
        producciones.put(7,  new Produccion("R", Arrays.asList("R", ">", "C")));
        producciones.put(8,  new Produccion("R", Arrays.asList("R", "<", "C")));
        producciones.put(9,  new Produccion("R", Arrays.asList("R", ">=", "C")));
        producciones.put(10, new Produccion("R", Arrays.asList("R", "<=", "C")));
        producciones.put(11, new Produccion("R", Arrays.asList("C")));
        producciones.put(12, new Produccion("C", Arrays.asList("C", "+", "D")));
        producciones.put(13, new Produccion("C", Arrays.asList("C", "-", "D")));
        producciones.put(14, new Produccion("C", Arrays.asList("D")));
        producciones.put(15, new Produccion("D", Arrays.asList("D", "*", "E")));
        producciones.put(16, new Produccion("D", Arrays.asList("D", "/", "E")));
        producciones.put(17, new Produccion("D", Arrays.asList("D", "%", "E")));
        producciones.put(18, new Produccion("D", Arrays.asList("E")));
        producciones.put(19, new Produccion("E", Arrays.asList("F", "**", "E")));
        producciones.put(20, new Produccion("E", Arrays.asList("F")));
        producciones.put(21, new Produccion("F", Arrays.asList("!", "F")));
        producciones.put(22, new Produccion("F", Arrays.asList("(", "A", ")")));
        producciones.put(23, new Produccion("F", Arrays.asList("id")));

        int i = 0;
        int paso = 1;
        System.out.println();
        System.out.println("================ TRAZA DEL ANALISIS ================");
        imprimirSeparador();
        imprimirFila("Paso", "Entrada pendiente", "Accion", "Estados", "Pila semantica", "Pila ensambladora");
        imprimirSeparador();

        while (i < entrada.size()) {
            int estado = pilaEstados.peek();
            String simbolo = entrada.get(i);
            String accion = tabla.getOrDefault(estado, new HashMap<>()).get(simbolo);

            if (accion == null) {
                System.out.printf("Paso %-2d Error sintactico: no hay accion para '%s' en estado %d%n",
                        paso, simbolo, estado);
                return;
            }

            String descripcion;

            if (accion.startsWith("d")) {
                // ---------- DESPLAZAMIENTO ----------
                int nuevoEstado = Integer.parseInt(accion.substring(1));
                pilaEstados.push(nuevoEstado);
                String tipo = simbolo.equals("id") && i < tiposEntrada.size()
                        ? tiposEntrada.get(i) : simbolo;
                pilaSem.push(valorTipado(simbolo, tipo));

                if (simbolo.equals("id")) {
                    // Se apila el idxx (id interno) en la pila_em
                    String lexema = lexemasEntrada.get(i);
                    String idInterno = idInternoDeLexema(lexema);
                    pilaEm.push(idInterno);
                    descripcion = "DESPLAZA " + accion + " con " + valorTipado(simbolo, tipo)
                            + " -> pila_em.push(" + idInterno + ")";
                } else {
                    // Operadores y paréntesis se apilan tal cual
                    pilaEm.push(simbolo);
                    descripcion = "DESPLAZA " + accion + " con " + valorTipado(simbolo, tipo)
                            + " -> pila_em.push(" + simbolo + ")";
                }
                i++;

            } else if (accion.startsWith("r")) {
                // ---------- REDUCCIÓN ----------
                int prod = Integer.parseInt(accion.substring(1));
                Produccion p = producciones.get(prod);

                List<String> valores = new ArrayList<>();
                for (int j = 0; j < p.derecha.size(); j++) {
                    pilaEstados.pop();
                    valores.add(0, pilaSem.pop());
                }

                // idxx del id desplazado (solo para r23)
                String idInterno = null;
                if (prod == 23) {
                    String lexema = lexemasEntrada.get(i - 1);
                    idInterno = idInternoDeLexema(lexema);
                }

                try {
                    String tipo = tipoDeReduccion(prod, valores);
                    pilaSem.push(valorTipado(p.izquierda, tipo));

                    String accionEm = actualizarPilaEm(prod, idInterno);
                    descripcion = String.format("REDUCE r%-2d %-18s => %s | pila_em: %s",
                            prod, p, tipo, accionEm);
                } catch (IllegalArgumentException error) {
                    System.out.printf("Paso %-2d Error semantico: %s%n", paso, error.getMessage());
                    return;
                }

                int estadoActual = pilaEstados.peek();
                String gotoAccion = tabla.get(estadoActual).get(p.izquierda);
                int nuevoEstado = Integer.parseInt(gotoAccion);
                pilaEstados.push(nuevoEstado);

            } else if (accion.equals("acc")) {
                System.out.println("-----------------------------------------------------------------------"
                        + "-----------------------------------------------------------------------");
                System.out.println("ACEPTADA. Tipo final: " + tipoDeValor(pilaSem.peek()));
                generarArchivos();
                return;

            } else {
                // GOTO
                try {
                    int nuevoEstado = Integer.parseInt(accion);
                    pilaEstados.push(nuevoEstado);
                    descripcion = "GOTO " + accion;
                } catch (NumberFormatException e) {
                    System.out.println("Accion invalida: " + accion);
                    return;
                }
            }

            imprimirFila(String.valueOf(paso), entradaPendiente(entrada, i),
                    descripcion, pilaEstados.toString(),
                    pilaSemanticaComoTexto(), pilaEnsambladoraComoTexto());
            paso++;
        }
    }

    // ---------------------------------------------------------------
    // Salida: código intermedio (triplos) y ensamblador
    // ---------------------------------------------------------------
    private void generarArchivos() throws IOException {
        // --- Código intermedio (triplos) ---
        File fIntermedio = new File("codigo_intermedio.txt");
        try (BufferedWriter w = new BufferedWriter(new FileWriter(fIntermedio))) {
            w.write("; Codigo intermedio (triplos) - FastDon");
            w.newLine();
            w.write(String.format("%-6s %-6s %-12s %-12s %-12s%n",
                    "Nro", "Op", "Op1", "Op2", "Resultado"));
            w.newLine();
            int n = 1;
            for (String[] t : triplos) {
                w.write(String.format("%-6d %-6s %-12s %-12s %-12s%n",
                        n++, t[0], t[1], t[2], t[3]));
            }
            w.newLine();
            w.write("; Temporales generados: " + contadorTemporales);
            w.newLine();
        }
        System.out.println("Codigo intermedio generado en: " + fIntermedio.getAbsolutePath());
        System.out.println("--- Triplos ---");
        int n = 1;
        for (String[] t : triplos) {
            System.out.printf("  (%d) %s = %s %s %s%n", n++, t[3], t[1], t[0], t[2]);
        }

        // --- Código ensamblador ---
        escribirEnsamblador(codigoEnsamblador);
    }

    private void escribirEnsamblador(List<String> instrucciones) throws IOException {
        File archivo = new File("codigo_ensamblador.asm");
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write("; Codigo ensamblador generado desde triplos - FastDon");
            escritor.newLine();
            escritor.write("; Temporales: " + contadorTemporales);
            escritor.newLine();
            for (String instruccion : instrucciones) {
                escritor.write(instruccion);
                escritor.newLine();
            }
            escritor.write("HALT");
            escritor.newLine();
        }
        System.out.println("Codigo ensamblador generado en: " + archivo.getAbsolutePath());
    }

    public void mostrarTabla() {
        for (Map.Entry<Integer, Map<String, String>> fila : tabla.entrySet()) {
            int estado = fila.getKey();
            Map<String, String> acciones = fila.getValue();
            System.out.print("Estado " + estado + ": ");
            for (Map.Entry<String, String> celda : acciones.entrySet()) {
                System.out.print(celda.getKey() + "=" + celda.getValue() + " | ");
            }
            System.out.println();
        }
    }

    // ---------------------------------------------------------------
    // Main
    // ---------------------------------------------------------------
    public static void main(String[] args) throws IOException {
        reservadas.add(new TABSIM("int", "reservada", "0", "idx", 0));
        reservadas.add(new TABSIM("String", "reservada", "0", "idx", 1));
        reservadas.add(new TABSIM("boolean", "reservada", "0", "idx", 2));
        AnalizadorSemantico parser = new AnalizadorSemantico();

        parser.registrarVariable("x", "int");
        parser.registrarVariable("y", "int");
        parser.registrarVariable("z", "boolean");
        parser.registrarVariable("s", "string");
        parser.registrarVariable("a", "int");
        parser.registrarVariable("b", "int");
        parser.registrarVariable("c", "int");
        parser.registrarVariable("d", "int");
        parser.registrarVariable("e", "int");
        parser.registrarVariable("f", "int");
        parser.registrarVariable("g", "int");
        parser.registrarVariable("h", "int");
        parser.registrarVariable("i", "int");
        parser.registrarVariable("j", "int");
        parser.registrarVariable("texto", "string");

        List<String> simbolos = Arrays.asList("Estado", "||", "&&", "==", "!=",
                ">", "<", ">=", "<=", "+", "-", "*", "/", "%", "**", "!",
                "(", ")", "id", "$", "A'", "A", "B", "R", "C", "D", "E", "F");

        parser.cargarTabla("src/AnalizadorLexico/GFastDon.csv", simbolos);

        System.out.print("Ingrese una expresion (ejemplo: (x + 3) > 2): ");
        String expresion = sc.nextLine();
        try {
            List<String> entrada = parser.tokenizarEntrada(expresion);
            System.out.println();
            System.out.println("=========================== ENTRADA ===========================");
            System.out.println("Original: " + expresion);
            parser.mostrarTokens(entrada);
            parser.parse(entrada);
        } catch (IllegalArgumentException error) {
            System.out.println("Error de entrada: " + error.getMessage());
        }
    }
}