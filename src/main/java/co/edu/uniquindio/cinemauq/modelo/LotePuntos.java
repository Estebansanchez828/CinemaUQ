package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

import java.time.LocalDate;

/**
 * Lote de puntos obtenido en una fecha (RN-11). Vence un año después.
 * Desde la fecha de vencimiento el lote ya no se puede usar.
 */
public class LotePuntos {

    private final int puntos;
    private int disponibles;
    private final LocalDate fechaObtencion;
    private final LocalDate fechaVencimiento;
    private final String codigoCompra;

    public LotePuntos(int puntos, LocalDate fechaObtencion, String codigoCompra) {
        this.puntos = puntos;
        this.disponibles = puntos;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaObtencion.plusYears(1);
        this.codigoCompra = codigoCompra;
    }

    public boolean estaVencido(LocalDate hoy) {
        return !hoy.isBefore(fechaVencimiento);
    }

    public void consumir(int cantidad) {
        if (cantidad > disponibles) {
            throw new DatoInvalidoException("El lote no tiene " + cantidad + " puntos disponibles.");
        }
        disponibles -= cantidad;
    }

    /** Deja el lote en 0 y devuelve cuántos puntos se anularon. */
    public int anular() {
        int anulados = disponibles;
        disponibles = 0;
        return anulados;
    }

    public String getEstado(LocalDate hoy) {
        if (estaVencido(hoy)) {
            return "Vencido";
        }
        return disponibles == 0 ? "Agotado" : "Vigente";
    }

    public int getPuntos() {
        return puntos;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getCodigoCompra() {
        return codigoCompra;
    }
}
