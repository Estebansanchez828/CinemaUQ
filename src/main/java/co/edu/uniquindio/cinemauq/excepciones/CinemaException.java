package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Excepción base del dominio CinemaUQ. Todas las reglas de negocio que se violan
 * lanzan una subclase de esta. Es no verificada (RuntimeException) para no
 * llenar las firmas de throws; el controlador la atrapa y la vista muestra una alerta.
 */
public class CinemaException extends RuntimeException {
    public CinemaException(String mensaje) {
        super(mensaje);
    }
}
