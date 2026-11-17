package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: un dato obligatorio falta o no es válido. */
public class DatoInvalidoException extends CinemaException {
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
