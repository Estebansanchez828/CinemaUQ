package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de ventas realizadas por periodo. */
public class ReporteVentas extends ReporteBase {

    public ReporteVentas(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Ventas por periodo";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Código", 9, "Fecha", 18, "Cliente", 16, "Función", 24, "Detalle", 26, "Pago", 13, "Valor", 12, "Estado", 12));
        int cantidad = 0;
        double totalTarjeta = 0;
        double descuentos = 0;
        int puntos = 0;
        for (Compra c : repositorio.getCompras()) {
            if (!c.fueVendida() || !enRango(c.getFecha().toLocalDate(), desde, hasta)) {
                continue;
            }
            cantidad++;
            totalTarjeta += c.getValorPagado();
            descuentos += c.getDescuento();
            puntos += c.getPuntosUsados();
            String pago = c.getMetodoPago() == MetodoPago.TARJETA ? "Tarjeta" : c.getPuntosUsados() + " pts";
            r.agregarRegistro(fila(c.getCodigo(), 9, Formato.fechaHora(c.getFecha()), 18, c.getCliente().getNombre(), 16,
                    c.getFuncion() == null ? "Solo confitería" : c.getFuncion().getPelicula().getTitulo(), 24,
                    c.getResumen(), 26, pago, 13, Formato.dinero(c.getValorPagado()), 12, c.getNombreEstado(), 12));
        }
        r.agregarTotal("Ventas realizadas: " + cantidad);
        r.agregarTotal("Total cobrado con tarjeta: " + Formato.dinero(totalTarjeta));
        r.agregarTotal("Descuentos por promociones: " + Formato.dinero(descuentos));
        r.agregarTotal("Puntos redimidos en compras: " + puntos);
        return r;
    }
}
