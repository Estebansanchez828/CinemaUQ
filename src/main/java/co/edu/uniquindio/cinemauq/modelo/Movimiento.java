package co.edu.uniquindio.cinemauq.modelo;

import java.time.LocalDateTime;

/** Movimiento de la tarjeta virtual: compra, recarga o reembolso. */
public class Movimiento {

    private final LocalDateTime fecha;
    private final TipoMovimiento tipo;
    private final double valor;
    private final double saldoResultante;
    private final String descripcion;

    public Movimiento(LocalDateTime fecha, TipoMovimiento tipo, double valor,
                      double saldoResultante, String descripcion) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valor = valor;
        this.saldoResultante = saldoResultante;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public double getSaldoResultante() {
        return saldoResultante;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
