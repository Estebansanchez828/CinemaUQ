package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de entradas vendidas por función y por película (según la fecha de la función). */
public class ReporteEntradas extends ReporteBase {

    public ReporteEntradas(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Entradas por película y función";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Función", 9, "Película", 26, "Sala", 13, "Fecha", 18, "Estado", 12, "Entradas", 10, "Valor entradas", 15));
        Map<String, Integer> porPelicula = new LinkedHashMap<>();
        int total = 0;
        double valorTotal = 0;
        for (Funcion f : repositorio.getFunciones()) {
            if (!enRango(f.getInicio().toLocalDate(), desde, hasta)) {
                continue;
            }
            int entradas = 0;
            double valor = 0;
            for (Compra c : repositorio.comprasDeFuncion(f)) {
                if (c.estaPagada()) {
                    entradas += c.getEntradas().size();
                    valor += c.getEntradas().stream().mapToDouble(Entrada::getPrecio).sum();
                }
            }
            total += entradas;
            valorTotal += valor;
            porPelicula.merge(f.getPelicula().getTitulo(), entradas, Integer::sum);
            r.agregarRegistro(fila(f.getId(), 9, f.getPelicula().getTitulo(), 26, f.getSala().getNombre(), 13,
                    Formato.fechaHora(f.getInicio()), 18, f.getEstadoTexto(), 12, entradas, 10, Formato.dinero(valor), 15));
        }
        r.agregarTotal("Entradas vendidas: " + total + " | Valor (precio de lista): " + Formato.dinero(valorTotal));
        porPelicula.forEach((pelicula, cantidad) -> r.agregarTotal("  " + pelicula + ": " + cantidad + " entradas"));
        return r;
    }
}
