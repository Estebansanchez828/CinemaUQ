package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de cancelaciones y reembolsos. */
public class ReporteReembolsos extends ReporteBase {

    public ReporteReembolsos(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Cancelaciones y reembolsos";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Fecha", 18, "Compra", 9, "Cliente", 18, "Pagado", 12, "%", 6, "Reembolso", 12, "Motivo", 34));
        int cantidad = 0;
        double total = 0;
        Map<String, Integer> porMotivo = new LinkedHashMap<>();
        for (Reembolso re : repositorio.getReembolsos()) {
            if (!enRango(re.getFecha().toLocalDate(), desde, hasta)) {
                continue;
            }
            cantidad++;
            total += re.getValor();
            porMotivo.merge(re.getMotivo(), 1, Integer::sum);
            r.agregarRegistro(fila(Formato.fechaHora(re.getFecha()), 18, re.getCompra().getCodigo(), 9,
                    re.getCompra().getCliente().getNombre(), 18, Formato.dinero(re.getCompra().getValorPagado()), 12,
                    re.getPorcentaje() + "%", 6, Formato.dinero(re.getValor()), 12, re.getMotivo(), 34));
        }
        r.agregarTotal("Cancelaciones: " + cantidad);
        r.agregarTotal("Valor reembolsado: " + Formato.dinero(total));
        porMotivo.forEach((motivo, n) -> r.agregarTotal("  " + motivo + ": " + n));
        return r;
    }
}
