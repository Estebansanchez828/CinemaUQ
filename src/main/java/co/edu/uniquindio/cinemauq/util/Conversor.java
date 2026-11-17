package co.edu.uniquindio.cinemauq.util;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/** Convierte el texto de los campos de la vista en números, con un mensaje claro si falla. */
public final class Conversor {

    private Conversor() {
    }

    public static double aDouble(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(".", "").replace(",", "."));
        } catch (RuntimeException e) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser un número.");
        }
    }

    public static int aEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (RuntimeException e) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser un número entero.");
        }
    }
}
