package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: un usuario que no es administrador intenta una operación de administración (RN-05). */
public class AccesoDenegadoException extends CinemaException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
