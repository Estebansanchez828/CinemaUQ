package co.edu.uniquindio.cinemauq.modelo;

import java.time.LocalDate;

/** Movimiento de la cuenta de puntos (sirve para el reporte de puntos acumulados y redimidos). */
public class MovimientoPuntos {

    private final LocalDate fecha;
    private final TipoMovimientoPuntos tipo;
    private final int cantidad;
    private final String descripcion;

    public MovimientoPuntos(LocalDate fecha, TipoMovimientoPuntos tipo, int cantidad, String descripcion) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public TipoMovimientoPuntos getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
