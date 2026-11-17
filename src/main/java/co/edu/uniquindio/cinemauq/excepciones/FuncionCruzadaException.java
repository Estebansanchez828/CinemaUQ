package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: dos funciones se cruzan en la misma sala (RN-14). */
public class FuncionCruzadaException extends CinemaException {
    public FuncionCruzadaException(String mensaje) {
        super(mensaje);
    }
}
