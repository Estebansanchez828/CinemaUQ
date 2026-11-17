package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.EstadoSolicitud;
import co.edu.uniquindio.cinemauq.modelo.Movimiento;
import co.edu.uniquindio.cinemauq.modelo.TipoMovimiento;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.enRango;
import static co.edu.uniquindio.cinemauq.reporte.ResultadoReporte.fila;

/** Reporte de recargas realizadas a las tarjetas virtuales. */
public class ReporteRecargas extends ReporteBase {

    public ReporteRecargas(RepositorioCinema repositorio) {
        super(repositorio);
    }

    @Override
    public String getNombre() {
        return "Recargas realizadas";
    }

    @Override
    public ResultadoReporte generar(LocalDate desde, LocalDate hasta) {
        ResultadoReporte r = new ResultadoReporte(getNombre(), desde, hasta,
                fila("Fecha", 18, "Cliente", 18, "Tarjeta", 12, "Valor", 12, "Saldo resultante", 17, "Descripción", 36));
        int cantidad = 0;
        double total = 0;
        Map<Movimiento, Cliente> recargas = new TreeMap<>(Comparator.comparing(Movimiento::getFecha)
                .thenComparing(Movimiento::hashCode));
        for (Cliente cliente : repositorio.getClientes()) {
            for (Movimiento m : cliente.getTarjeta().getMovimientos()) {
                if (m.getTipo() == TipoMovimiento.RECARGA && enRango(m.getFecha().toLocalDate(), desde, hasta)) {
                    recargas.put(m, cliente);
                }
            }
        }
        for (Map.Entry<Movimiento, Cliente> e : recargas.entrySet()) {
            Movimiento m = e.getKey();
            Cliente cliente = e.getValue();
            cantidad++;
            total += m.getValor();
            r.agregarRegistro(fila(Formato.fechaHora(m.getFecha()), 18, cliente.getNombre(), 18,
                    cliente.getTarjeta().getNumero(), 12, Formato.dinero(m.getValor()), 12,
                    Formato.dinero(m.getSaldoResultante()), 17, m.getDescripcion(), 36));
        }
        long pendientes = repositorio.getSolicitudes().stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE).count();
        r.agregarTotal("Recargas realizadas: " + cantidad);
        r.agregarTotal("Valor total recargado: " + Formato.dinero(total));
        r.agregarTotal("Solicitudes pendientes hoy: " + pendientes);
        return r;
    }
}
