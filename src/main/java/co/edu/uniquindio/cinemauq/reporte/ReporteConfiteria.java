package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de productos y combos vendidos. */
public class ReporteConfiteria extends ReporteBase {

    public ReporteConfiteria(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Productos y combos vendidos";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Ítem", 26, "Tipo", 10, "Cantidad", 10, "Valor (lista)", 15));
        Map<ItemConfiteria, Integer> conteo = new LinkedHashMap<>();
        for (Compra c : repositorio.getCompras()) {
            if (c.estaPagada() && enRango(c.getFecha().toLocalDate(), desde, hasta)) {
                c.getItems().forEach(item -> conteo.merge(item, 1, Integer::sum));
            }
        }
        int unidades = 0;
        double valor = 0;
        int productos = 0;
        int combos = 0;
        for (Map.Entry<ItemConfiteria, Integer> e : conteo.entrySet()) {
            ItemConfiteria item = e.getKey();
            int cantidad = e.getValue();
            unidades += cantidad;
            valor += item.getPrecio() * cantidad;
            if ("Combo".equals(item.getTipo())) {
                combos += cantidad;
            } else {
                productos += cantidad;
            }
            r.agregarRegistro(fila(item.getNombre(), 26, item.getTipo(), 10, cantidad, 10,
                    Formato.dinero(item.getPrecio() * cantidad), 15));
        }
        r.agregarTotal("Unidades vendidas: " + unidades + " (productos: " + productos + ", combos: " + combos + ")");
        r.agregarTotal("Valor a precio de lista: " + Formato.dinero(valor));
        return r;
    }
}
