/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import static AnalizadorLexico.analizadorPartTwo.reservadas;
import java.io.*;
import java.util.*;

/**
 *
 * @author Jaime Ramirez
 */

/*
primero se debe ingresar el ejercicio a evalualar 
depues las variables se convierten en ids por ejemplo x = id0
se inica la pila en 0, despues se evalua el valor de la pila con el simbolo del ejecicio ejem
0 -> ( se busca en la tabla si es una reduccion o un desplazamiento, en cada reduccion o desplazamiento se cambia el valor de la pila
con los desplazamientos se a�ade el valor del desplazamiento ejem d5 -> pila 0 + 5
y con las reducciones se identifica el valor con eso se sabe cuantos digitos se debe recorrer la pila por ejem
r -> t esto quiere decir que se debe recorer una pocision ( si la pila esta como 05 entonces se debe recorrer hasta el cero)
una vez obtenido el valor en el que quedo se evalua con el simbolo ejem 0 con ( y se identifica la siguiente operacion para contuinar...
En la pila-sem la pila empieza con un espacio vacio (_), por cada desplazamiento se a�ade un espacio vacio a la pila, con cada reduccion se evalua dos casos
en el primero es cuando se hace una reduccion de un operador entonces pasa igual y cuando se le hace a un id se obtiene el tipo del dato ( int, string, boolean)
despues se pone en la pila ejem se hace una reduccion de un entero en la pila quedaria algo asi _I y por ultimo si se hace una reduccion y tiene una operacion
se evalua ejem tenemos en la pila _I_I y la reduccion es tal que esta R -> R + T ( se haria la suma) entonces se obtiene los tipos de datos que en este caso ambos
son enteros se hace una suma de enteros, en caso de que se tenga que hacer una operacion de distintos tipos se debe imprimir que ha ocurrido un error semantico.
 */
public class AnalizadorSemantico {

    public static Scanner sc = new Scanner(System.in);

    // Pila de estados
    private Stack<Integer> pilaEstados = new Stack<>();
    // Pila sem�ntica
    private Stack<String> pilaSem = new Stack<>();
    private Map<Integer, Map<String, String>> tabla = new HashMap<>();
    private Map<String, String> tablaTipos = new HashMap<>(); // id ? tipo
    private Map<String, String> nombresAId = new HashMap<>();
    private List<String> tiposEntrada = new ArrayList<>();
    
    

    // Convierte variables a IDs y guarda su tipo
    public void registrarVariable(String nombre, String tipo) {
        String id = "id" + tablaTipos.size();
        tablaTipos.put(id, tipo);
        nombresAId.put(nombre, id);
        System.out.println("Variable " + nombre + " registrada como " + id + " de tipo " + tipo);
    }

    /** Convierte la expresion escrita por el usuario a los simbolos de la tabla LR. */
    public List<String> tokenizarEntrada(String expresion) {
        List<String> tokens = new ArrayList<>();
        tiposEntrada.clear();
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
                posicion += operador.length();
                continue;
            }

            if (actual == '$') {
                tokens.add("$");
                tiposEntrada.add("$");
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
                } else if (nombresAId.containsKey(lexema)) {
                    tokens.add("id");
                    tiposEntrada.add(tablaTipos.get(nombresAId.get(lexema)));
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
                continue;
            }

            throw new IllegalArgumentException("Simbolo no reconocido en la posicion " + posicion + ": " + actual);
        }
        if (tokens.isEmpty() || !tokens.get(tokens.size() - 1).equals("$")) {
            tokens.add("$");
            tiposEntrada.add("$");
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

    private String tipoDeValor(String valor) {
        int separador = valor.indexOf(':');
        return separador >= 0 ? valor.substring(separador + 1) : valor;
    }

    private String valorTipado(String simbolo, String tipo) {
        return simbolo + ":" + tipo;
    }

    private String pilaSemanticaComoTexto() {
        return String.join(" | ", pilaSem);
    }

    private String entradaPendiente(List<String> entrada, int posicion) {
        if (posicion >= entrada.size()) {
            return "<vacia>";
        }
        return String.join(" ", entrada.subList(posicion, entrada.size()));
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

    // Cargar tabla LR desde CSV
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

    // Simulaci�n del parser
    public void parse(List<String> entrada) {
        pilaEstados.clear();
        pilaSem.clear();
        pilaEstados.push(0);
        pilaSem.push("_");
        // Registro de los productos
        Map<Integer, Produccion> producciones = new HashMap<>();

        producciones.put(1, new Produccion("A", Arrays.asList("A", "||", "B")));
        producciones.put(2, new Produccion("A", Arrays.asList("B")));
        producciones.put(3, new Produccion("B", Arrays.asList("B", "&&", "R")));
        producciones.put(4, new Produccion("B", Arrays.asList("R")));
        producciones.put(5, new Produccion("R", Arrays.asList("R", "==", "C")));
        producciones.put(6, new Produccion("R", Arrays.asList("R", "!=", "C")));
        producciones.put(7, new Produccion("R", Arrays.asList("R", ">", "C")));
        producciones.put(8, new Produccion("R", Arrays.asList("R", "<", "C")));
        producciones.put(9, new Produccion("R", Arrays.asList("R", ">=", "C")));
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
        System.out.printf("%-5s %-24s %-30s %-20s %s%n",
            "Paso", "Entrada pendiente", "Accion", "Estados", "Pila semantica");
        System.out.println("-----------------------------------------------------"
            + "-----------------------------------------------");
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
            if (accion.startsWith("d")) { // desplazamiento
                int nuevoEstado = Integer.parseInt(accion.substring(1));
                pilaEstados.push(nuevoEstado);
                String tipo = simbolo.equals("id") && i < tiposEntrada.size()
                        ? tiposEntrada.get(i) : simbolo;
                pilaSem.push(valorTipado(simbolo, tipo));
                descripcion = "DESPLAZA " + accion + " con " + valorTipado(simbolo, tipo);
                i++;
            } else if (accion.startsWith("r")) { // reducci�n
                int prod = Integer.parseInt(accion.substring(1));
                Produccion p = producciones.get(prod);
                List<String> valores = new ArrayList<>();
                for (int j = 0; j < p.derecha.size(); j++) {
                    pilaEstados.pop();
                    valores.add(0, pilaSem.pop());
                }
                try {
                    String tipo = tipoDeReduccion(prod, valores);
                    pilaSem.push(valorTipado(p.izquierda, tipo));
                    descripcion = String.format("REDUCE r%-2d %-18s => %s", prod, p, tipo);
                } catch (IllegalArgumentException error) {
                    System.out.printf("Paso %-2d Error semantico: %s%n", paso, error.getMessage());
                    return;
                }

                int estadoActual = pilaEstados.peek();
                String gotoAccion = tabla.get(estadoActual).get(p.izquierda);
                int nuevoEstado = Integer.parseInt(gotoAccion);
                pilaEstados.push(nuevoEstado);

            } else if (accion.equals("acc")) {
                System.out.println("-----------------------------------------------------"
                        + "-----------------------------------------------");
                System.out.println("ACEPTADA. Tipo final: " + tipoDeValor(pilaSem.peek()));
                return;
            } else {
                // Aqu� es un GOTO (n�mero puro)
                try {
                    int nuevoEstado = Integer.parseInt(accion);
                    pilaEstados.push(nuevoEstado);
                    descripcion = "GOTO " + accion;
                } catch (NumberFormatException e) {
                    System.out.println("Accion invalida: " + accion);
                    return;
                }
            }

            System.out.printf("%-5d %-24s %-30s %-20s %s%n", paso,
                    entradaPendiente(entrada, i), descripcion, pilaEstados, pilaSemanticaComoTexto());
            paso++;
        }
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

    public static void main(String[] args) throws IOException {
        reservadas.add(new TABSIM("int", "reservada", "0", "idx", 0));
        reservadas.add(new TABSIM("String", "reservada", "0", "idx", 1));
        reservadas.add(new TABSIM("boolean", "reservada", "0", "idx", 2));
        AnalizadorSemantico parser = new AnalizadorSemantico();

        // Registrar variables
        parser.registrarVariable("x", "int");
        parser.registrarVariable("y", "int");
        parser.registrarVariable("z", "boolean");
        parser.registrarVariable("s", "string");

        // Definir s�mbolos de la tabla (orden de columnas en tu CSV)
        List<String> simbolos = Arrays.asList("Estado", "||", "&&", "==", "!=",
                ">", "<", ">=", "<=", "+", "-", "*", "/", "%", "**", "!",
                "(", ")", "id", "$", "A'", "A", "B", "R", "C", "D", "E", "F");

        // Cargar tabla desde archivo CSV
        parser.cargarTabla("src/AnalizadorLexico/GFastDon.csv", simbolos);
        //parser.mostrarTabla();

        System.out.print("Ingrese una expresion (ejemplo: (x + 3) > 2): ");
        String expresion = sc.nextLine();
        try {
            List<String> entrada = parser.tokenizarEntrada(expresion);
            System.out.println();
            System.out.println("================ ENTRADA =================");
            System.out.println("Original: " + expresion);
            parser.mostrarTokens(entrada);
            parser.parse(entrada);
        } catch (IllegalArgumentException error) {
            System.out.println("Error de entrada: " + error.getMessage());
        }
    }

    /*public static void LeerEjercicio(){
        System.out.println("Ingerese el ejercio a evaluar:");
        String Ejer = sc.nextLine();
    } 
     */
}
