package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: no hay puntos vigentes suficientes para redimir (RN-12). */
public class PuntosInsuficientesException extends CinemaException {
    public PuntosInsuficientesException(String mensaje) {
        super(mensaje);
    }
}
