package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: el nombre de usuario ya está registrado (RN-13). */
public class UsuarioYaExisteException extends CinemaException {
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje);
    }
}
