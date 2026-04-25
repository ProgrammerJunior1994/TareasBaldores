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
            texto = texto.replaceAll("\\)", " )"); // Agregar un espacio antes de un cierre de paréntesis

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
        analizadorPartTwo.limpiarArvhivo();
//        System.out.println(System.getProperty("user.dir"));
        System.out.println("Bienvenidos a mi programa, ¿Cómo están?");
        try {
            File archivo = new File("CodigoLimpio.txt"); // Crear instancia del archivo .txt para una mejor referencia hacia el
            if (!archivo.exists()) {
                System.out.println("Error: El archivo no se encuentra en la ruta: " + System.getProperty("user.dir")); // Dentro de este if, se valida si existe el archivo .txt en la ruta, si no existe se manda un error en consola junto con la ruta para que sea más fácil para el usuario solucionar el problema.
            } else {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
//                    System.out.println(linea);  // while para leer línea por línea el archivo limpio
                    String[] tokens = linea.split(" "); // Crear tokens del lexema (divide la cadena por cada espacio)
                    for (String t : tokens) {
                        System.out.println(t);  // for para mostrar los tokens (opcional mostrarlos, solo se muestran para pruebas)
                        switch (t) {
                            case "int":
                                String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
                                if (t.matches(varentera)) {
                                    System.out.println("!!!Declaracion valida!!!");
                                } else {
                                    System.out.println("Declaracion invalida.");
                                }

                                break;
                            default:
                                throw new AssertionError();
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
}
