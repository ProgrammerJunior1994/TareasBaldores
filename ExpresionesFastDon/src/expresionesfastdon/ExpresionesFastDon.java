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
                System.out.println("1. Validar Tipos de Datos.");
                System.out.println("2. Validar Estructuras Condicionales.");
                System.out.println("3. Validar Estructuras de Repeticion.");
                System.out.println("4. Validar función principal.");
                System.out.println("5. Validar estructuras de E/S.");
                System.out.println("Presiona 0 para salir del programa.");
                opcExpre = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: opción no válida..");
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
                        System.out.println("1. Declaración de una variable Entera (int).");
                        System.out.println("2. Declaración de una variable de Cadena (String).");
//                        System.out.println("3. Declaracion de una variable doble (double)."); // Eliminar números con números decimal y flotantes (Double y Float)
//                        System.out.println("4. Declaracion de una variable flotante (float).");
                        System.out.println("3. Declaración de una variable Booleana (boolean).");
                        System.out.println("Presiona 0 para salir.");
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
                                System.out.println("Declaracion de variable entera valida");
//                                error = false;
                            } else {
                                System.out.println("Declaracion invalida.");
                                error = true;
                            }
                            break;

                        case 2:
                            System.out.println("Ingresa la declaracion de una variable de cadena: ");
                            String cadena = sc.nextLine();
                            String varcadena = "^\\s*String\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*\"[^\"]*\")?\\s*;\\s*$";
                            if (cadena.matches(varcadena)) {
                                System.out.println("Declaracion de variable String válida");
//                                error = false;
                            } else {
                                System.out.println("Declaracion invalida.");
                                error = true;
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
                        case 3:
                            System.out.println("Ingresa la declaracion de una variable booleana: ");
                            String booleano = sc.nextLine();
                            String varBoolean = "^\\s*boolean\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*=\\s*(true|false))?\\s*;\\s*$";
                            if (booleano.matches(varBoolean)) {
                                System.out.println("Declaración de variable booleana válida");
//                                error = false;
                            } else {
                                System.out.println("Declaración de variabla booleana inválida.");
                                error = true;
                            }
                            break;

                        case 0:
                            error = true;
                            break;
                    }
                    break;

                case 2:
                    try {
                        System.out.println("\t===||Prueba de expresiones de FastDon||===");
                        System.out.println("Ingresa la Estructura condicional a verificar.");
                        System.out.println("1. Estructura condicional if.");
                        System.out.println("2. Estructura condicional Switch.");
                        System.out.println("3. Estructura else.");
                        System.out.println("Presiona 0 para salir.");
                        opcEC = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opción no válida..");
                        sc.nextLine();
                        opcEC = -1;
                        continue;
                    }
                    sc.nextLine();
                    switch (opcEC) {
                        case 1:
                            System.out.println("Ingresa una estructura if:");
                            String ifElse = sc.nextLine();
                           String varIf = "^\\s*if\\s*\\(\\s*[a-zA-Z_][a-zA-Z0-9_]+(\\s*[+\\-*/%]\\s*[a-zA-Z0-9_]+)*\\s*(==|!=|<=|>=|<|>)\\s*[a-zA-Z0-9_]+(\\s*(&&|\\|\\|)\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\s*[+\\-*/%]\\s*[a-zA-Z0-9_]+)*\\s*(==|!=|<=|>=|<|>)\\s*[a-zA-Z0-9_]+)*\\s*\\)\\s*\\{\\s*$";
                            if (ifElse.matches(varIf)) {
                                System.out.println("Declaración de estructura if válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura if invalida.");
                                error = true;
                            }
                            break;
                        case 2:
                            System.out.println("Ingresa una estructura switch:");
                            String estructuraSwitch = sc.nextLine();
                            String varSwitch = "^\\s*switch\\s*\\(\\s*[a-zA-Z_][a-zA-Z0-9_]*\\s*\\)\\s*\\{\\s*$";
                            if (estructuraSwitch.matches(varSwitch)) {
                                System.out.println("Declaración de estructura switch válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura switch invalida.");
                                error = true;
                            }
                            break;
                        case 3:
                            System.out.println("Ingresa la estructura else:");
                            String estructuraElse = sc.nextLine();
                            String varElse = "^\\s*else\\s*\\{\\s*$";
                            if (estructuraElse.matches(varElse)) {
                                System.out.println("Declaración de estructura else válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura else invalida.");
                                error = true;
                            }
                        case 0:
                            error = true;
                            break;
                    }
                    break;
                case 3:
                    try {
                        System.out.println("\t===||Prueba de expresiones de FastDon||===");
                        System.out.println("Ingresa la Estructura de Repeticion a verificar."); // Que valide operadores aritmÃ©ticos 
                        System.out.println("1. Estructura de Repeticion For.");
                        System.out.println("2. Estructura de Repeticion While.");
                        System.out.println("3. Estructura de Repeticion Do-While.");
                        System.out.println("Presione 0 para salir.");
                        opcER = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opción no válida..");
                        sc.nextLine();
                        opcER = -1;
                        continue;
                    }
                    sc.nextLine();

                    switch (opcER) {

                        case 1:
                            System.out.println("Ingresa una estructura for:");
                            String forE = sc.nextLine();
                            String varFor = "^\\s*for\\s*\\(\\s*int\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*\\d+\\s*;\\s*\\1\\s*(==|!=|<=|>=|<|>)\\s*\\d+\\s*;\\s*\\1(\\+\\+|--)\\s*\\)\\s*\\{\\s*$";
                            if (forE.matches(varFor)) {
                                System.out.println("Declaración de estructura for válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura for inválida.");
                                error = true;
                            }
                            break;

                        case 2:
                            System.out.println("Ingresa una estructura while:");
                            String whilE = sc.nextLine();
                            String varWhile = "^\\s*while\\s*\\(\\s*[a-zA-Z0-9_+\\+\\-*/%<>=!&|()\\s]+\\s*\\)\\s*\\{\\s*$";
                            if (whilE.matches(varWhile)) {
                                System.out.println("Declaración de estructura while válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura while inválida.");
                                error = true;
                            }
                            break;
//
                        case 3:
                            System.out.println("Ingresa una estructura do-while:");
                            String doWhile = sc.nextLine();
                            String varDoWhile = "^\\s*do\\s*\\{\\s*$";
                            if (doWhile.matches(varDoWhile)) {
                                System.out.println("Declaración de estructura do-while válida");
//                                error = false;
                            } else {
                                System.out.println("Estructura do-while inválida.");
                                error = true;
                            }
                            break;
                        case 0:
                            error = true;
                            break;
                    }
                    break;
                case 4:
                    System.out.println("\t===||Prueba de expresiones de FastDon||===");
                    System.out.println("1. Validar clase main.");
                    System.out.println("Presione 0 para salir");
                    int opcM = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Ingresa la clase main");
                    String main = sc.nextLine();
                    String varMain = "^\\s*main\\s*\\{\\s*$";
                    if (main.matches(varMain)) {
                        System.out.println("Declaracion de clase main válida");
//                        error = false;
                    } else {
                        System.out.println("Estructura main inválida.");
                        error = true;
                    }
                    break;
                case 5:
                    int opcES = 0;
                    try {
                        System.out.println("\t===||Prueba de expresiones de FastDon||===");
                        System.out.println("Ingresa la Estructura de E/S."); // Que valide operadores aritmÃ©ticos 
                        System.out.println("1. Estructura de entrada.");
                        System.out.println("2. Estructura de salida.");
                        System.out.println("Presiona 0 para salir.");
                        opcES = sc.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error: opción no válida..");
                        sc.nextLine();
                        opcES = -1;
                        continue;
                    }
                    sc.nextLine();
                    switch (opcES) {
                        case 1:
                            System.out.println("Ingresa la estructura de entrada:");
                            String entrada = sc.nextLine();
                            String varInput = "^\\s*input(S|I|B)\\s*\\(\\s*[a-zA-Z_][a-zA-Z0-9_]*\\s*\\)\\s*;\\s*$";
                            if (entrada.matches(varInput)) {
                                System.out.println("Estructura de entrada válida.");
//                                error = false;
                            } else {
                                System.out.println("Estructura de entrada inválida.");
                                error = true;
                            }
                            break;
                        case 2:
                            System.out.println("Ingresa la estructura de salida:");
                            String print = sc.nextLine();
                            String varPrint = "^\\s*print\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
                            if (print.matches(varPrint)) {
                                System.out.println("Estructura de salida válida.");
//                                error = false;
                            } else {
                                System.out.println("Estructura de salida inválida.");
                                error = true;
                            }
                            break;
                        case 0:
                            error = true;

                            break;
                    }
                    break;
                case 0:
                    System.out.println("Adios..");
                    error = false;
                    break;
                default:
                    System.out.println("Error: opción no válida");
                    break;
            }

        } while (error != false);
    }
}
