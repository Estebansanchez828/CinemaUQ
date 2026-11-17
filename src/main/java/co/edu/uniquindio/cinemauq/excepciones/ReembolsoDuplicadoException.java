package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: la compra ya fue reembolsada (RN-10). */
public class ReembolsoDuplicadoException extends CinemaException {
    public ReembolsoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
