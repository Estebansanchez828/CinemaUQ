package co.edu.uniquindio.cinemauq.servicio.evento;

import co.edu.uniquindio.cinemauq.util.Formato;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Observer – rol: Observador concreto.
 * Lleva las estadísticas de la sesión actual (ventas y reembolsos en vivo) y una bitácora.
 */
public class ObservadorEstadisticas implements ObservadorCompra {

    private int ventas;
    private double ingresos;
    private int reembolsos;
    private double valorReembolsado;
    private final List<String> bitacora = new ArrayList<>();

    @Override
    public void actualizar(EventoCompra evento) {
        if (evento.getTipo() == TipoEvento.COMPRA_PAGADA) {
            ventas++;
            ingresos += evento.getValor();
        } else {
            reembolsos++;
            valorReembolsado += evento.getValor();
        }
        bitacora.add(0, Formato.fechaHora(evento.getFecha()) + " - " + evento.getTipo() + " - "
                + evento.getCompra().getCodigo() + " - " + Formato.dinero(evento.getValor()));
    }

    public int getVentas() {
        return ventas;
    }

    public double getIngresos() {
        return ingresos;
    }

    public int getReembolsos() {
        return reembolsos;
    }

    public double getValorReembolsado() {
        return valorReembolsado;
    }

    public List<String> getBitacora() {
        return bitacora;
    }
}
