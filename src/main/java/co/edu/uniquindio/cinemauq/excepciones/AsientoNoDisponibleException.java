package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: el asiento ya está vendido para la función (RN-02). */
public class AsientoNoDisponibleException extends CinemaException {
    public AsientoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
