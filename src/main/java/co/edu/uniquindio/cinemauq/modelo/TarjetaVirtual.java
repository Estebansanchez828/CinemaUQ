package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.SaldoInsuficienteException;
import co.edu.uniquindio.cinemauq.excepciones.TarjetaInactivaException;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tarjeta virtual del cliente.
 * SOLID – SRP: solo maneja el saldo, el estado y los movimientos de la tarjeta.
 */
public class TarjetaVirtual {

    private final String numero;
    private double saldo;
    private boolean activa = true;
    private final List<Movimiento> movimientos = new ArrayList<>();

    public TarjetaVirtual(String numero) {
        this.numero = numero;
    }

    /** RN-03: antes de confirmar se verifica que esté activa y tenga saldo. */
    public void verificarPago(double valor) {
        if (!activa) {
            throw new TarjetaInactivaException("La tarjeta " + numero + " está inactiva.");
        }
        if (saldo < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente: el total es " + Formato.dinero(valor)
                    + " y el saldo es " + Formato.dinero(saldo) + ". Puede solicitar una recarga.");
        }
    }

    /** RN-04: descuenta el valor y registra el movimiento. */
    public void descontar(double valor, String descripcion, LocalDateTime fecha) {
        verificarPago(valor);
        saldo -= valor;
        movimientos.add(new Movimiento(fecha, TipoMovimiento.COMPRA, valor, saldo, descripcion));
    }

    /** RN-05 y RN-21: la recarga debe ser positiva y la tarjeta debe estar activa. */
    public void recargar(double valor, String descripcion, LocalDateTime fecha) {
        if (valor <= 0) {
            throw new DatoInvalidoException("El valor de la recarga debe ser mayor que 0.");
        }
        if (!activa) {
            throw new TarjetaInactivaException("No se puede recargar: la tarjeta " + numero + " está inactiva.");
        }
        saldo += valor;
        movimientos.add(new Movimiento(fecha, TipoMovimiento.RECARGA, valor, saldo, descripcion));
    }

    /** RN-10: el reembolso siempre vuelve a esta tarjeta. */
    public void abonarReembolso(double valor, String descripcion, LocalDateTime fecha) {
        saldo += valor;
        movimientos.add(new Movimiento(fecha, TipoMovimiento.REEMBOLSO, valor, saldo, descripcion));
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }
}
