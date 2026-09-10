/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;

import java.util.List;

/**
 *
 * @author Jaime Ramirez
 */
public class Produccion {
    String izquierda;
    List<String> derecha;

    Produccion(String izquierda, List<String> derecha) {
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    public String toString() {
        return izquierda + " -> " + String.join(" ", derecha);
    }
}
