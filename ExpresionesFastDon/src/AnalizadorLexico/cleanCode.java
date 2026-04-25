package AnalizadorLexico;

import java.io.*;

public class cleanCode {

    public static void limpiarArchivo() {
        try (
                InputStream is = cleanCode.class.getResourceAsStream("entrada.txt"); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.out.println("No se encontrÃ³ entrada.txt en el paquete.");
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
        limpiarArchivo();
    }
}
