package co.edu.uniquindio.cinemauq.servicio.fachada;

import java.util.List;

/** Resumen de una compra antes de pagar (RF-006): detalle, subtotal, descuento, total y puntos. */
public class Cotizacion {

    private final List<String> detalle;
    private final double subtotal;
    private final double descuento;
    private final int puntosNecesarios;

    public Cotizacion(List<String> detalle, double subtotal, double descuento, int puntosNecesarios) {
        this.detalle = detalle;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.puntosNecesarios = puntosNecesarios;
    }

    public List<String> getDetalle() {
        return detalle;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return subtotal - descuento;
    }

    public int getPuntosNecesarios() {
        return puntosNecesarios;
    }
}
