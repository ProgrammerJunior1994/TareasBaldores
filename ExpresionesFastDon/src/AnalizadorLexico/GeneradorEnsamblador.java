package AnalizadorLexico;

import java.io.IOException;

public class GeneradorEnsamblador {
    private final Compilador8086 compilador;

    public GeneradorEnsamblador() {
        compilador = new Compilador8086();
    }

    public void generar(String codigoFuente, String archivoSalida) throws IOException {
        compilador.compilar(codigoFuente, archivoSalida);
    }

    public void compilar(String codigoFuente, String archivoSalida) throws IOException {
        generar(codigoFuente, archivoSalida);
    }
}
