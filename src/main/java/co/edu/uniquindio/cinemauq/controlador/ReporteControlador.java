package co.edu.uniquindio.cinemauq.controlador;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.reporte.ExportadorTxt;
import co.edu.uniquindio.cinemauq.reporte.Reporte;
import co.edu.uniquindio.cinemauq.reporte.ReporteConfiteria;
import co.edu.uniquindio.cinemauq.reporte.ReporteEntradas;
import co.edu.uniquindio.cinemauq.reporte.ReporteOcupacion;
import co.edu.uniquindio.cinemauq.reporte.ReportePuntos;
import co.edu.uniquindio.cinemauq.reporte.ReporteRecargas;
import co.edu.uniquindio.cinemauq.reporte.ReporteReembolsos;
import co.edu.uniquindio.cinemauq.reporte.ReporteVentas;
import co.edu.uniquindio.cinemauq.reporte.ResultadoReporte;
import co.edu.uniquindio.cinemauq.servicio.Sistema;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/** Controlador (MVC) de reportes: el administrador elige el tipo y el rango de fechas (RF-026, RF-027). */
public class ReporteControlador {

    private final List<Reporte> reportes;
    private final ExportadorTxt exportador = new ExportadorTxt();
    private ResultadoReporte ultimo;

    public ReporteControlador(Sistema sistema) {
        RepositorioCinema repo = sistema.getRepositorio();
        reportes = List.of(new ReporteVentas(repo), new ReporteEntradas(repo), new ReporteOcupacion(repo),
                new ReporteConfiteria(repo), new ReporteRecargas(repo), new ReporteReembolsos(repo),
                new ReportePuntos(repo));
    }

    public List<Reporte> tipos() {
        return reportes;
    }

    /** Polimorfismo: se llama igual a generar() sin importar qué reporte sea. */
    public String generar(Reporte reporte, LocalDate desde, LocalDate hasta) {
        if (reporte == null || desde == null || hasta == null) {
            throw new DatoInvalidoException("Seleccione el tipo de reporte y el rango de fechas.");
        }
        if (desde.isAfter(hasta)) {
            throw new DatoInvalidoException("La fecha inicial no puede ser posterior a la final.");
        }
        ultimo = reporte.generar(desde, hasta);
        return exportador.formatear(ultimo);
    }

    public Path exportar() {
        if (ultimo == null) {
            throw new DatoInvalidoException("Primero genere un reporte.");
        }
        return exportador.exportar(ultimo);
    }
}
