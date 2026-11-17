package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: faltan menos de 2 horas o la compra no se puede cancelar (RN-08, RN-22, RN-24). */
public class CancelacionNoPermitidaException extends CinemaException {
    public CancelacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
