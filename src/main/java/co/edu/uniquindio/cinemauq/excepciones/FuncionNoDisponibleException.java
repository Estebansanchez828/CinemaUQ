package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: la función está cancelada o ya empezó (RN-15). */
public class FuncionNoDisponibleException extends CinemaException {
    public FuncionNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
