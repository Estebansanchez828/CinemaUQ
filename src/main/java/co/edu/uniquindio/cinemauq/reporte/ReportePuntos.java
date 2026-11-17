package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.LotePuntos;
import co.edu.uniquindio.cinemauq.modelo.MovimientoPuntos;
import co.edu.uniquindio.cinemauq.modelo.TipoMovimientoPuntos;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de puntos acumulados y redimidos por los clientes. */
public class ReportePuntos extends ReporteBase {

    public ReportePuntos(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Puntos acumulados y redimidos";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Fecha", 12, "Cliente", 18, "Tipo", 13, "Puntos", 9, "Descripción", 44));
        Map<TipoMovimientoPuntos, Integer> totales = new EnumMap<>(TipoMovimientoPuntos.class);
        LocalDate hoy = LocalDate.now();
        int vigentes = 0;
        int vencidos = 0;
        for (Cliente cliente : repositorio.getClientes()) {
            for (MovimientoPuntos m : cliente.getCuentaPuntos().getMovimientos()) {
                if (enRango(m.getFecha(), desde, hasta)) {
                    totales.merge(m.getTipo(), m.getCantidad(), Integer::sum);
                    r.agregarRegistro(fila(Formato.fecha(m.getFecha()), 12, cliente.getNombre(), 18, m.getTipo(), 13,
                            m.getCantidad(), 9, m.getDescripcion(), 44));
                }
            }
            vigentes += cliente.getCuentaPuntos().getSaldo(hoy);
            for (LotePuntos lote : cliente.getCuentaPuntos().getLotes()) {
                if (lote.estaVencido(hoy)) {
                    vencidos += lote.getDisponibles();
                }
            }
        }
        for (TipoMovimientoPuntos tipo : TipoMovimientoPuntos.values()) {
            r.agregarTotal("Puntos " + tipo.toString().toLowerCase() + ": " + totales.getOrDefault(tipo, 0));
        }
        r.agregarTotal("Saldo vigente de todos los clientes hoy: " + vigentes);
        r.agregarTotal("Puntos vencidos sin usar: " + vencidos);
        return r;
    }
}
