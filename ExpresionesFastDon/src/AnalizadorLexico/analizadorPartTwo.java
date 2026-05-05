/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author br340
 */
public class analizadorPartTwo {

    public static void limpiarArvhivo() {
        try (
                InputStream is = analizadorPartTwo.class.getResourceAsStream("entrada.txt"); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.out.println("No se encuentra entrada.txt en el paquete.");
                return;
            }

            // Crear archivo de salida (ya procesado)
            File archivoSalida = new File("CodigoLimpio.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida));

            StringBuilder contenido = new StringBuilder();
            String linea;

            // Leer codigo fuente
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            String texto = contenido.toString();

            // Quitar comentarios
            texto = texto.replaceAll("(?s)/\\*.*?\\*/", "");
            texto = texto.replaceAll("//.*", "");

            texto = texto.replaceAll("\\(", "( "); // Agregar un espacio después de un inicio de paréntesis
            texto = texto.replaceAll("\\(", " ("); // Agregar un espacio antes de un cierre de paréntesis
            texto = texto.replaceAll("\\)", ") "); // Agregar un espacio antes de un cierre de paréntesis
            texto = texto.replaceAll("\\)", " )"); // Agregar un espacio antes de un cierre de paréntesis
            texto = texto.replaceAll("\\;", " ;"); // Agregar un espacio antes de un punto y coma
            texto = texto.replaceAll("\\:", " :"); // Agregar un espacio antes de un dos puntos
            texto = texto.replaceAll("\"", "\" "); // Agregar un espacio después de una comillas dobles
            texto = texto.replaceAll("\"", " \""); // Agregar un espacio antes de una comillas dobles

            // Quitar tabulaciones
            texto = texto.replaceAll("\\t", " ");

            // Tener solo 1 espacio
            texto = texto.replaceAll(" +", " ");

            // Eliminar saltos de lineas (lineas en blanco)
            String[] lineas = texto.split("\n");

            for (String l : lineas) {
                l = l.trim();
                if (l.isEmpty()) {
                    continue;
                }

                bw.write(l);
                bw.newLine();
            }

            bw.close();

            System.out.println("Archivo creado en: " + archivoSalida.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Declaración de elementos del TABSIM
//        TABSIM entero = new TABSIM("int", "int", "0", 0);
//        TABSIM cadena = new TABSIM("String", "String", "0", 1); //Primera prueba para la creación de elementos del TABSIM (buena idea pero puede ser mejor)
//        TABSIM booleano = new TABSIM("boolean", "boolean", "0", 2);

        // Creación de elementos en el TABSIM
        TABSIM[] reservadas = {
            new TABSIM("int", "int", "0", 0),
            new TABSIM("String", "String", "0", 1),
            new TABSIM("boolean", "boolean", "0", 2),
            new TABSIM("main", "main", "0", 3),
            new TABSIM("if", "if", "0", 4),
            new TABSIM("else", "else", "0", 5),
            new TABSIM("for", "for", "0", 6),
            new TABSIM("whie", "while", "0", 7),
            new TABSIM("else", "else", "0", 8),
            new TABSIM("else", "else", "0", 9)

        };

        System.out.println(reservadas[0].var);
        System.out.println(reservadas[1].var);
        System.out.println(reservadas[2].var);
        analizadorPartTwo.limpiarArvhivo();
//        System.out.println(System.getProperty("user.dir"));
        System.out.println("Bienvenidos a mi programa, ¿Cómo están?"); // Impresión para probar acentos en consola
        try {
            File archivo = new File("CodigoLimpio.txt"); // Crear instancia del archivo .txt para una mejor referencia hacia el
            if (!archivo.exists()) {
                System.out.println("Error: El archivo no se encuentra en la ruta: " + System.getProperty("user.dir")); // Dentro de este if, se valida si existe el archivo .txt en la ruta, si no existe se manda un error en consola junto con la ruta para que sea más fácil para el usuario solucionar el problema.
            } else {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
//                    System.out.println(linea);  // while para mostrar la  lectura de línea por línea el archivo limpio
                    String[] tokens = linea.split(" "); // Crear tokens del lexema (divide la cadena por cada espacio)
                    for (String t : tokens) {
                        System.out.println(t);  // for para mostrar los tokens (opcional mostrarlos, solo se muestran para pruebas)
                        switch (t) {
                            case "int":
                                validarLexemaEntero(linea, t);
//                                String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
//                                if (linea.matches(varentera)) {
//                                    // El lexema es válido
//                                    if (t.equals(reservadas[0].var)) {
//                                        
//                                    }
//                                } else {
//                                    System.out.println("Declaracion invalida.");
//                                }
                                break;
                            default:

                        }
//                        if(t.equals("int")){
//                            String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
//                            if (t.matches(varentera)) {
//                                System.out.println("!!!Declaracion valida!!!");
//                            } else {
//                                System.out.println("Declaracion invalida.");
//                            }
                    }
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }       

    static void validarLexemaEntero(String l, String t) {
        TABSIM[] reservadas = {
            new TABSIM("int", "int", "0", 0),
            new TABSIM("String", "String", "0", 1),
            new TABSIM("boolean", "boolean", "0", 2),
            new TABSIM("main", "main", "0", 3),
            new TABSIM("if", "if", "0", 4),
            new TABSIM("else", "else", "0", 5),
            new TABSIM("for", "for", "0", 6),
            new TABSIM("whie", "while", "0", 7),
            new TABSIM("else", "else", "0", 8),
            new TABSIM("else", "else", "0", 9)

        };

        String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
        if (l.matches(varentera)) {
            // El lexema es válido
            for (int i = 0; i < reservadas.length; i++) {
                if (t.equals(reservadas[i].var)) {
                    // Si existe en el TABSIM, hace nada...
                    System.out.println("TOKEN YA DECLARADO EN EL TABSIM!!");
                } else {
                    System.out.println("TOKEN NO DECLARADO EN EL TABSIM");
                }
            }
        } else {
            System.out.println("Error en la linea []");
        }
    }
}
