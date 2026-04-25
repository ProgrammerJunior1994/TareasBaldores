package expresionesfastdon;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ExpresionesFastDon {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opc = 0;
        int opcExpre = 0;
        int opcEC = 0;
        int opcER = 0;
        boolean error = true;

        do {
            try {
                System.out.println("Elige una expresion para validarla");
                System.out.println("1. Verificar la declaracion de Tipos de Datos.");
                System.out.println("2. Verificar la declaracion de las Estructuras Condicionales.");
                System.out.println("3. Verificar la declaracion de las Estructuras de Repeticion.");
                System.out.println("Presiona 0 para salir del programa.");
                opcExpre = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: opcion no valida..");
                sc.nextLine();
                opcExpre = -1;
                continue;
            }
            sc.nextLine();

            switch (opcExpre) {

                case 1:
                    try {
                        System.out.println("\t===||Prueba de expresiones de FastDon||===");
                        System.out.println("Ingresa una expresion para verificar:");
                        System.out.println("1. Declaracion de una variable Entera (int).");
                        System.out.println("2. Declaracion de una variable de Cadena (String).");
//                        System.out.println("3. Declaracion de una variable doble (double)."); // Eliminar números con números decimal y flotantes (Double y Float)
//                        System.out.println("4. Declaracion de una variable flotante (float).");
                        System.out.println("5. Declaracion de una variable Booleana (boolean).");
                        System.out.println("Presiona 0 para salir del programa.");
                        opc = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opcion no valida..");
                        sc.nextLine();
                        opc = -1;
                        continue;
                    }
                    sc.nextLine();

                    switch (opc) {

                        case 1:
                            System.out.println("Ingresa la declaracion de una variable entera: ");
                            String entera = sc.nextLine();
                            String varentera = "^\\s*int\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+)?\\s*;\\s*$";
                            if (entera.matches(varentera)) {
                                System.out.println("!!!Declaracion valida!!!");
                            } else {
                                System.out.println("Declaracion invalida.");
                            }
                            break;

                        case 2:
                            System.out.println("Ingresa la declaracion de una variable de cadena: ");
                            String cadena = sc.nextLine();
                            String varcadena = "^\\s*String\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*\"[^\"]*\")?\\s*;\\s*$";
                            if (cadena.matches(varcadena)) {
                                System.out.println("!!!Declaracion valida!!!");
                            } else {
                                System.out.println("Declaracion invalida.");
                            }
                            break;

//                        case 3:
//                            System.out.println("Ingresa la declaracion de una variable doble: ");
//                            String doble = sc.nextLine();
//                            String varDouble = "^\\s*double\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+(\\.\\d+)?)?\\s*;\\s*$";
//                            if (doble.matches(varDouble)) {
//                                System.out.println("!!!Declaracion valida!!!");
//                            } else {
//                                System.out.println("Declaracion invalida.");
//                            }
//                            break;
//
//                        case 4:
//                            System.out.println("Ingresa la declaracion de una variable flotante: ");
//                            String flotante = sc.nextLine();
//                            String varfloat = "^\\s*float\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*-?\\d+(\\.\\d+)?)?\\s*;\\s*$";
//                            if (flotante.matches(varfloat)) {
//                                System.out.println("!!!Declaracion valida!!!");
//                            } else {
//                                System.out.println("Declaracion invalida.");
//                            }
//                            break;

                        case 5:
                            System.out.println("Ingresa la declaracion de una variable booleana: ");
                            String booleano = sc.nextLine();
                            String varBoolean = "^\\s*boolean\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*(true|false))?\\s*;\\s*$";
                            if (booleano.matches(varBoolean)) {
                                System.out.println("!!!Declaracion valida!!!");
                            } else {
                                System.out.println("Declaracion invalida.");
                            }
                            break;

                        case 0:
                            System.out.println("Saliendo del submenu...");
                            break;

                        default:
                            System.out.println("Declaracion invalida");
                            break;
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Ingresa la Estructura condicional a verificar.");
                        System.out.println("1. Estructura condicional if-else.");
                        System.out.println("2. Estructura condicional Switch.");
                        opcEC = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opcion no valida..");
                        sc.nextLine();
                        opcEC = -1;
                        continue;
                    }
                    sc.nextLine();

                    switch (opcEC) {

                        case 1:
                            System.out.println("Ingresa una estructura if-else:");
                            String ifElse = sc.nextLine();
                            String varIfElse = "^\\s*if\\s*\\([^)]*\\)\\s*\\{[^}]*\\}\\s*else\\s*\\{[^}]*\\}\\s*$";
                            if (ifElse.matches(varIfElse)) {
                                System.out.println("!!!Declaracion de estructura if-else valida!!!");
                            } else {
                                System.out.println("Estructura if-else invalida.");
                            }
                            break;

                        case 2:
                            System.out.println("Ingresa una estructura switch:");
                            String estructuraSwitch = sc.nextLine();
                            String varSwitch = "^\\s*switch\\s*\\([^)]*\\)\\s*\\{\\s*(case\\s+[^:]+:\\s*[^;]*;\\s*break;\\s*)+\\s*(default:\\s*[^;]*;\\s*)?\\}\\s*$";
                            if (estructuraSwitch.matches(varSwitch)) {
                                System.out.println("!!Declaracion de estructura switch valida!!!");
                            } else {
                                System.out.println("Estructura switch invalida.");
                            }
                            break;

                        default:
                            System.out.println("Error: opcion no valida.");
                            break;
                    }
                    break;

                case 3:
                    try {
                        System.out.println("Ingresa la Estructura de Repeticion a verificar."); // Que valide operadores aritméticos 
                        System.out.println("1. Estructura de Repeticion For.");
                        System.out.println("2. Estructura de Repeticion While.");
                        System.out.println("3. Estructura de Repeticion Do-While.");
                        opcER = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opcion no valida..");
                        sc.nextLine();
                        opcER = -1;
                        continue;
                    }
                    sc.nextLine();

                    switch (opcER) {

                        case 1:
                            System.out.println("Ingresa una estructura for:");
                            String forE = sc.nextLine();
                            String varFor = "^\\s*for\\s*\\(\\s*[^;]*;\\s*[^;]*;\\s*[^)]*\\)\\s*\\{[^}]*\\}\\s*$";
                            if (forE.matches(varFor)) {
                                System.out.println("!!!Declaracion de estructura for valida!!!");
                            } else {
                                System.out.println("Estructura for invalida.");
                            }
                            break;

                        case 2:
                            System.out.println("Ingresa una estructura while:");
                            String whilE = sc.nextLine();
                            String varWhile = "^\\s*while\\s*\\([^)]*\\)\\s*\\{[^}]*\\}\\s*$";
                            if (whilE.matches(varWhile)) {
                                System.out.println("!!!Declaracion de estructura while valida!!!");
                            } else {
                                System.out.println("Estructura while invalida.");
                            }
                            break;

                        case 3:
                            System.out.println("Ingresa una estructura do-while:");
                            String doWhile = sc.nextLine();
                            String varDoWhile = "^\\s*do\\s*\\{[^}]*\\}\\s*while\\s*\\([^)]*\\)\\s*;\\s*$";
                            if (doWhile.matches(varDoWhile)) {
                                System.out.println("!!!Declaracion de estructura do-while valida!!!");
                            } else {
                                System.out.println("Estructura do-while invalida.");
                            }
                            break;

                        default:
                            System.out.println("Error: opcion no valida.");
                            break;
                    }
                    break;

                case 0:
                    error = false;
                    break;

                default:
                    System.out.println("Error: opcion no valida.");
                    break;
            }

        } while (error != false);
    }
}
