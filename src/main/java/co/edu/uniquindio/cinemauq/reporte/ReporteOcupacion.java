package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.EstadoFuncion;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de ocupación de las salas por función. */
public class ReporteOcupacion extends ReporteBase {

    public ReporteOcupacion(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Ocupación de salas";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Sala", 13, "Película", 26, "Fecha", 18, "Estado", 12, "Ocupados", 10, "Capacidad", 11, "Ocupación", 10));
        Map<String, int[]> porSala = new LinkedHashMap<>();
        for (Funcion f : repositorio.getFunciones()) {
            if (!enRango(f.getInicio().toLocalDate(), desde, hasta)) {
                continue;
            }
            int ocupados = f.getAsientosOcupados().size();
            int capacidad = f.getSala().getCapacidad();
            if (f.getEstado() != EstadoFuncion.CANCELADA) {
                int[] acumulado = porSala.computeIfAbsent(f.getSala().getNombre(), k -> new int[2]);
                acumulado[0] += ocupados;
                acumulado[1] += capacidad;
            }
            r.agregarRegistro(fila(f.getSala().getNombre(), 13, f.getPelicula().getTitulo(), 26,
                    Formato.fechaHora(f.getInicio()), 18, f.getEstadoTexto(), 12, ocupados, 10, capacidad, 11,
                    String.format("%.1f %%", f.getOcupacion()), 10));
        }
        porSala.forEach((sala, v) -> r.agregarTotal(sala + ": " + v[0] + " de " + v[1] + " asientos = "
                + String.format("%.1f %%", v[1] == 0 ? 0 : v[0] * 100.0 / v[1])));
        return r;
    }
}
