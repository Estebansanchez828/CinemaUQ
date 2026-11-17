package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: la operación no es válida en la situación actual (RN-25, RN-26). */
public class OperacionNoPermitidaException extends CinemaException {
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
