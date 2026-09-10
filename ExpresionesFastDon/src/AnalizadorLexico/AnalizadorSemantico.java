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
con los desplazamientos se añade el valor del desplazamiento ejem d5 -> pila 0 + 5
y con las reducciones se identifica el valor con eso se sabe cuantos digitos se debe recorrer la pila por ejem
r -> t esto quiere decir que se debe recorer una pocision ( si la pila esta como 05 entonces se debe recorrer hasta el cero)
una vez obtenido el valor en el que quedo se evalua con el simbolo ejem 0 con ( y se identifica la siguiente operacion para contuinar...
En la pila-sem la pila empieza con un espacio vacio (_), por cada desplazamiento se añade un espacio vacio a la pila, con cada reduccion se evalua dos casos
en el primero es cuando se hace una reduccion de un operador entonces pasa igual y cuando se le hace a un id se obtiene el tipo del dato ( int, string, boolean)
despues se pone en la pila ejem se hace una reduccion de un entero en la pila quedaria algo asi _I y por ultimo si se hace una reduccion y tiene una operacion
se evalua ejem tenemos en la pila _I_I y la reduccion es tal que esta R -> R + T ( se haria la suma) entonces se obtiene los tipos de datos que en este caso ambos
son enteros se hace una suma de enteros, en caso de que se tenga que hacer una operacion de distintos tipos se debe imprimir que ha ocurrido un error semantico.
 */
public class AnalizadorSemantico {

    public static Scanner sc = new Scanner(System.in);

    // Pila de estados
    private Stack<Integer> pilaEstados = new Stack<>();
    // Pila semántica
    private Stack<String> pilaSem = new Stack<>();
    private Map<Integer, Map<String, String>> tabla = new HashMap<>();
    private Map<String, String> tablaTipos = new HashMap<>(); // id ? tipo

    // Convierte variables a IDs y guarda su tipo
    public void registrarVariable(String nombre, String tipo) {
        String id = "id" + tablaTipos.size();
        tablaTipos.put(id, tipo);
        System.out.println("Variable " + nombre + " registrada como " + id + " de tipo " + tipo);
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

    // Simulación del parser
    public void parse(List<String> entrada) {
        pilaEstados.push(0);
        pilaSem.push("_");

        int i = 0;
        while (i < entrada.size()) {
            int estado = pilaEstados.peek();
            String simbolo = entrada.get(i);

            String accion = tabla.getOrDefault(estado, new HashMap<>()).get(simbolo);

            if (accion == null) {
                System.out.println("Error sintáctico en: " + simbolo);
                return;
            }

            if (accion.startsWith("d")) { // desplazamiento
                int nuevoEstado = Integer.parseInt(accion.substring(1));
                pilaEstados.push(nuevoEstado);
                pilaSem.push("_");
                i++;
            } else if (accion.startsWith("r")) { // reducción
                int prod = Integer.parseInt(accion.substring(1));
                // aplicar producción semántica
            } else if (accion.equals("acc")) {
                System.out.println("Cadena aceptada");
                return;
            } else {
                // Aquí es un GOTO (número puro)
                try {
                    int nuevoEstado = Integer.parseInt(accion);
                    pilaEstados.push(nuevoEstado);
                } catch (NumberFormatException e) {
                    System.out.println("Acción inválida: " + accion);
                    return;
                }
            }

            System.out.println("Pila estados: " + pilaEstados);
            System.out.println("Pila semántica: " + pilaSem);
        }
    }

    public static void main(String[] args) throws IOException {
        reservadas.add(new TABSIM("int", "reservada", "0", "idx", 0));
        reservadas.add(new TABSIM("String", "reservada", "0", "idx", 1));
        reservadas.add(new TABSIM("boolean", "reservada", "0", "idx", 2));
        AnalizadorSemantico parser = new AnalizadorSemantico();

        // Registrar variables
        parser.registrarVariable("x", "int");
        parser.registrarVariable("y", "boolean");

        // Definir símbolos de la tabla (orden de columnas en tu CSV)
        List<String> simbolos = Arrays.asList("Estado", "||", "&&", "==", "!=",
                ">", "<", ">=", "<=", "+", "-", "*", "/", "%", "**", "!",
                "(", ")", "id", "$", "A'", "A", "B", "R", "C", "D", "E", "F");

        // Cargar tabla desde archivo CSV
        parser.cargarTabla("src/AnalizadorLexico/GFastDon.csv", simbolos);

        // Ejemplo de entrada: id0 + id1 $
        List<String> entrada = Arrays.asList("id", "+", "id", "$");
        parser.parse(entrada);
    }

    /*public static void LeerEjercicio(){
        System.out.println("Ingerese el ejercio a evaluar:");
        String Ejer = sc.nextLine();
    } 
     */
}
