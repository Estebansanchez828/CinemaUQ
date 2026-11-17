package co.edu.uniquindio.cinemauq.modelo;

import java.time.LocalDateTime;

/** Reembolso de una compra (RN-06 a RN-10). */
public class Reembolso {

    private final int id;
    private final Compra compra;
    private final double valor;
    private final int porcentaje;
    private final String motivo;
    private final LocalDateTime fecha;

    public Reembolso(int id, Compra compra, double valor, int porcentaje, String motivo, LocalDateTime fecha) {
        this.id = id;
        this.compra = compra;
        this.valor = valor;
        this.porcentaje = porcentaje;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public Compra getCompra() {
        return compra;
    }

    public double getValor() {
        return valor;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
