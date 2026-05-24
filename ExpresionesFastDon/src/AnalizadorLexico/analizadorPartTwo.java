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
import java.util.ArrayList;

/**
 *
 * @author br340
 */
public class analizadorPartTwo {

//    static TABSIM[] reservadas = {
//        new TABSIM("int", "int", "0", 0),
//        new TABSIM("String", "String", "0", 1),
//        new TABSIM("boolean", "boolean", "0", 2),
//        new TABSIM("main", "main", "0", 3),
//        new TABSIM("if", "if", "0", 4),
//        new TABSIM("else", "else", "0", 5),
//        new TABSIM("for", "for", "0", 6),
//        new TABSIM("whie", "while", "0", 7)
//
//    };
    static ArrayList<TABSIM> reservadas = new ArrayList<>();

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
            texto = texto.replaceAll("\\[", " \\["); // Agregar un espacio antes de un inicio de corchete
            texto = texto.replaceAll("\\[", "\\[ "); // Agregar un espacio después de un inicio de corchete
            texto = texto.replaceAll("\\]", "\\] "); // Agregar un espacio después de un cierre de corchete
            texto = texto.replaceAll("\\]", " \\]"); // Agregar un espacio antes de un cierre de corchete
            texto = texto.replaceAll("=", "= "); // Agregar un espacio antes de un signo de igual
            texto = texto.replaceAll("=", " ="); // Agregar un espacio antes de un signo de igual
            texto = texto.replaceAll("==", "= "); // Agregar un espacio antes de un signo doble de igual
            texto = texto.replaceAll("==", " ="); // Agregar un espacio antes de un signo doble de igual

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
        reservadas.add(new TABSIM("int", "reservada", "0", "idx", 0));
        reservadas.add(new TABSIM("String", "reservada", "0", "idx", 1));
        reservadas.add(new TABSIM("boolean", "reservada", "0", "idx", 2));
        reservadas.add(new TABSIM("main", "reservada", "0", "idx", 3));
        reservadas.add(new TABSIM("if", "reservada", "0", "idx", 4));
        reservadas.add(new TABSIM("else", "reservada", "0", "idx", 5));
        reservadas.add(new TABSIM("for", "reservada", "0", "idx", 6));
        reservadas.add(new TABSIM("whie", "reservada", "0", "idx", 7));
        reservadas.add(new TABSIM("{", "reservada", "0", "idx", 8));
        reservadas.add(new TABSIM("}", "reservada", "0", "idx", 9));
        reservadas.add(new TABSIM("(", "reservada", "0", "idx", 10));
        reservadas.add(new TABSIM(")", "reservada", "0", "idx", 11));
        reservadas.add(new TABSIM("+", "reservada", "0", "idx", 12));
        reservadas.add(new TABSIM("-", "reservada", "0", "idx", 13));
        reservadas.add(new TABSIM("*", "reservada", "0", "idx", 14));
        reservadas.add(new TABSIM("/", "reservada", "0", "idx", 15));
        reservadas.add(new TABSIM("\"", "reservada", "0", "idx", 16));
        reservadas.add(new TABSIM("=", "reservada", "0", "idx", 17));
        reservadas.add(new TABSIM("==", "reservada", "0", "idx", 18));
        reservadas.add(new TABSIM("!", "reservada", "0", "idx", 19));
        reservadas.add(new TABSIM(";", "reservada", "0", "idx", 20));
        // Declaración de elementos del TABSIM
//        TABSIM entero = new TABSIM("int", "int", "0", 0);
//        TABSIM cadena = new TABSIM("String", "String", "0", 1); //Primera prueba para la creación de elementos del TABSIM (buena idea pero puede ser mejor)
//        TABSIM booleano = new TABSIM("boolean", "boolean", "0", 2);
        // Creación de elementos en el TABSIM
//        System.out.println(reservadas.get(0).var);
//        System.out.println(reservadas.get(1).var);
//        System.out.println(reservadas.get(2).var);
        analizadorPartTwo.limpiarArvhivo();
        System.out.println("");
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println("Bienvenidos a mi programa, ¿Cómo están?"); // Impresión para probar acentos en consola
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
//                        System.out.println(t);  // for para mostrar los tokens (opcional mostrarlos, solo se muestran para pruebas)
                        switch (t) {
                            case "int":

                                validarLexemaInt(linea, tokens);
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
                            case "String":
                                validarLexemaString(linea, tokens);
                                break;
                            case "boolean":
                                validarLexemaBoolean(linea, tokens);
                                break;
                            case "for":
                                validarLexemaFor(linea, tokens);
                                break;
                            case "switch":

                                break;
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

    static void validarLexemaInt(String l, String[] tokens) {
//        TABSIM[] reservadas = {
//            new TABSIM("int", "int", "0", 0),
//            new TABSIM("String", "String", "0", 1),
//            new TABSIM("boolean", "boolean", "0", 2),
//            new TABSIM("main", "main", "0", 3),  // Ya no hay necesidad de declararlo en cada función
//            new TABSIM("if", "if", "0", 4),
//            new TABSIM("else", "else", "0", 5),
//            new TABSIM("for", "for", "0", 6),
//            new TABSIM("whie", "while", "0", 7)
//
//        };

        String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
        if (l.matches(varentera)) {
            boolean existe = false;
            System.out.println(tokens[0]);
            for (int i = 0; i < reservadas.size(); i++) {
                if (tokens[1].equals(reservadas.get(i).var)) {
                    System.out.println("¡Error: Variable redeclarada!");
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                String valor = "0";
                if (tokens.length == 5) {
                    valor = tokens[3];
                    reservadas.add(new TABSIM(tokens[1], "variable", "0", "idx", reservadas.size()));

                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;

                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                    reservadas.add(new TABSIM(tokens[3], "valor", valor, "idx", reservadas.size()));
                    tokens[3] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[3]);
                    System.out.println(tokens[4]);
                } else {
                    valor = "0";
                    reservadas.add(new TABSIM(tokens[1], "variable", valor, "idx", reservadas.size()));
                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                }
            }

//            for (String t : tokens) {
//                boolean existe = false;
//
//                for (int i = 0; i < reservadas.size(); i++) {
//                    //Verificar si el token está dentro (Palabras reservadas)
//                    if (t.equals(reservadas.get(i).var) && reservadas.get(i).tipo.equals("reservada")) {
//                        existe = true;
//                        System.out.println(t);
//                        break;
//                    } else if (t.equals(reservadas.get(i).var) && reservadas.get(i).tipo.equals("variable")) {
//                        if (t.matches("^\\d+$")) {
//                        } else {
//                            existe = true;
//                            System.out.println("Error: Variable declarada más de una vez.");
//                            break;
//                        }
//                    }
//                }
//                if (!existe) {
//                    reservadas.add(new TABSIM(t, "variable", tokens[3], "idx", reservadas.size())); // Guardar el nombre de la variable al TABSIM
//                    for (int i = 0; i < reservadas.size(); i++) {
//                        if (t.equals(reservadas.get(i).var) && reservadas.get(i).tipo.equals("variable")) {
//                            t = reservadas.get(i).idx + reservadas.get(i).id;
//                            System.out.println(t);
//                        }
//                    }
//                }
//            }
            // El lexema es válido
//            for (int i = 0; i < reservadas.size(); i++) {
//                if (t.equals(reservadas.get(i).var)) {
//                    existe = true;
//                    break;
//                }
//            }
//            if (existe) {
//                System.out.println("VARIABLE YA DECLARADA EN EL TABSIM");
//            } else {
//                reservadas.add(new TABSIM(t, t, "0", reservadas.size()));
//                System.out.println("ID: " + reservadas.get(0).id);
//                System.out.println("TOKEN AÑADIDO AL TABSIM");
//            }
        } else {
            System.out.println("Error: Línea de código no escrita corretamente.");
        }
    }

    //Descomentar desde aquí
    static void validarLexemaString(String l, String[] tokens) {
        String varcadena = "^\\s*String\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*\"[^\"]*\")?\\s*;\\s*$";
        if (l.matches(varcadena)) {
            boolean existe = false;
            System.out.println(tokens[0]);
            for (int i = 0; i < reservadas.size(); i++) {
                if (tokens[1].equals(reservadas.get(i).var)) {
                    System.out.println("¡Error: Variable redeclarada!");
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                String valor = "0";
                if (tokens.length == 5) {
                    valor = tokens[3];
                    reservadas.add(new TABSIM(tokens[1], "variable", "0", "idx", reservadas.size()));
                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                    reservadas.add(new TABSIM(tokens[3], "valor", valor, "idx", reservadas.size()));
                    tokens[3] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[3]);
                    System.out.println(tokens[4]);
                } else {
                    valor = "0";
                    reservadas.add(new TABSIM(tokens[1], "variable", valor, "idx", reservadas.size()));
                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                }
            }
            //            // El lexema es válido
            //            for (int i = 0; i < reservadas.size(); i++) {
            //                if (t.equals(reservadas.get(i).var)) {
            //                    existe = true;
            //                    break;
            //                }
            //            }
            //            if (existe) {
            //                System.out.println("VARIABLE YA DECLARADA EN EL TABSIM");
            //            } else {
            //                reservadas.add(new TABSIM(t, t, "0", reservadas.size()));
            //                System.out.println("TOKEN AÑADIDO AL TABSIM");
            //            }
            //        } else {
            //            System.out.println("Error en la linea []");
        }
    }

    //
    static void validarLexemaBoolean(String l, String[] tokens) {
        String varboolean = "^\\s*boolean\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*(true|false))?\\s*;\\s*$";
        if (l.matches(varboolean)) {
            boolean existe = false;
            System.out.println(tokens[0]);
            for (int i = 0; i < reservadas.size(); i++) {
                if (tokens[1].equals(reservadas.get(i).var)) {
                    System.out.println("¡Error: Variable redeclarada!");
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                String valor = "0";
                if (tokens.length == 5) {
                    valor = tokens[3];
                    reservadas.add(new TABSIM(tokens[1], "variable", "0", "idx", reservadas.size()));
                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                    reservadas.add(new TABSIM(tokens[3], "valor", valor, "idx", reservadas.size()));
                    tokens[3] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[3]);
                    System.out.println(tokens[4]);
                } else {
                    valor = "0";
                    reservadas.add(new TABSIM(tokens[1], "variable", valor, "idx", reservadas.size()));
                    tokens[1] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
                    System.out.println(tokens[1]);
                    System.out.println(tokens[2]);
                }
            }
            //            // El lexema es válido
            //            for (int i = 0; i < reservadas.size(); i++) {
            //                if (t.equals(reservadas.get(i).var)) {
            //                    existe = true;
            //                    break;
            //                }
            //            }
            //            if (existe) {
            //                System.out.println("VARIABLE YA DECLARADA EN EL TABSIM");
            //            } else {
            //                reservadas.add(new TABSIM(t, t, "0", reservadas.size()));
            //                System.out.println("TOKEN AÑADIDO AL TABSIM");
            //            }
            //        } else {
            //            System.out.println("Error en la linea []");
        }
    }
    //
//        static void validarLexemaMain(String l, String[] tokens) {
//            String regex = "^\\s*main\\s*\\(\\s*\\)\\s*\\{.*\\}\\s*$";
//
//    if(l.matches(regex)){
//        System.out.println("Main válido");
//    } else {
//        System.out.println("Main inválido");
//    }
//    //            // El lexema es válido
//    //            for (int i = 0; i < reservadas.size(); i++) {
//    //                if (t.equals(reservadas.get(i).var)) {
//    //                    existe = true;
//    //                    break;
//    //                }
//    //            }
//    //            if (existe) {
//    //                System.out.println("VARIABLE YA DECLARADA EN EL TABSIM");
//    //            } else {
//    //                reservadas.add(new TABSIM(t, t, "0", reservadas.size()));
//    //                System.out.println("TOKEN AÑADIDO AL TABSIM");
//    //            }
//    //        } else {
//    //            System.out.println("Error en la linea []");
//    
//        }
    //
    //    static void validarAll(String l, String t) {
    //        boolean existe = false;
    //        for (int i = 0; i < reservadas.size(); i++) {
    //            if (t.equals(reservadas.get(i).var)) {
    //                existe = true;
    //                break;
    //            }
    //        }
    //        if (existe) {
    //            System.out.println(t + " DECLARADO DOS VECES!!!!!!!");
    //        } else {
    //            reservadas.add(new TABSIM(t, t, "0", reservadas.size()));
    //            System.out.println(t + " AÑADIDO AL TABSIM");
    //        }
    //
    //    }
    //Descomentar aquí
    //    static void validarLexemaMain(String l, String t) {
    //        if (l.matches(varboolean)) {
    //            // El lexema es válido
    //            for (int i = 0; i < reservadas.length; i++) {
    //                if (t.equals(reservadas[i].var)) {
    //                    // Si existe en el TABSIM, hace nada...
    //                    System.out.println("TOKEN YA DECLARADO EN EL TABSIM!!");
    //                } else {
    //                    System.out.println("TOKEN NO DECLARADO EN EL TABSIM");
    //
    //                }
    //            }
    //        } else {
    //            System.out.println("Error en la linea []");
    //        }
    //    }

    static void validarLexemaFor(String l, String[] tokens) {
        String varFor = "^\\s*for\\s*\\(\\s*[^;]*;\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\s*[+\\-*/]\\s*[a-zA-Z0-9_]+)*\\s*(==|!=|<=|>=|<|>)\\s*[a-zA-Z0-9_]+\\s*;\\s*[^)]*\\)\\s*\\{[^}]*\\s*$";
        for (int i = 0; i < 10; i++) {

        }
        if (l.matches(varFor)) {
            boolean existe = true;
            System.out.println(tokens[0]);
            System.out.println(tokens[1]);
            System.out.println(tokens[2]);
            for (int i = 0; i < reservadas.size(); i++) {
                if (tokens[3].equals(reservadas.get(i).var)) {
                    tokens[3] = reservadas.get(i).idx + reservadas.get(i).id;
                    System.out.println(tokens[3]);
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                    reservadas.add(new TABSIM(tokens[3], "variable", "0", "idx", reservadas.size()));
                    tokens[3] = reservadas.get(reservadas.size()-1).idx + reservadas.get(reservadas.size()-1).id;
                    System.out.println(tokens[3]);
                }
            String valor = tokens[5];
            System.out.println(tokens[4]);
            reservadas.add(new TABSIM(tokens[5], "valor", valor, "idx", reservadas.size()));
            tokens[5] = reservadas.get(reservadas.size() - 1).idx + reservadas.get(reservadas.size() - 1).id;
            System.out.println(tokens[5]);

        } else {
            System.out.println("Error: estructura for mal declarada");
        }
    }

}
