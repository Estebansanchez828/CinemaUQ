package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: usuario o contraseña incorrectos. */
public class CredencialesInvalidasException extends CinemaException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
