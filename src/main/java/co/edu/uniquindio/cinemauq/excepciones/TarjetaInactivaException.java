package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: la tarjeta está bloqueada (RN-03, RN-21). */
public class TarjetaInactivaException extends CinemaException {
    public TarjetaInactivaException(String mensaje) {
        super(mensaje);
    }
}
