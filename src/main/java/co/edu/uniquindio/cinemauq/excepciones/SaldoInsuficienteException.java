package co.edu.uniquindio.cinemauq.excepciones;

/** Excepción del dominio: la tarjeta no tiene saldo para pagar (RN-03). */
public class SaldoInsuficienteException extends CinemaException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
