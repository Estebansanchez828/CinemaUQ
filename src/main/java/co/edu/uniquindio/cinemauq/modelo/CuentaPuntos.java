package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.PuntosInsuficientesException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Cuenta de puntos CinemaUQ del cliente, organizada por lotes.
 * SOLID – SRP: solo administra lotes de puntos (acumular, redimir, anular, devolver).
 */
public class CuentaPuntos {

    private final List<LotePuntos> lotes = new ArrayList<>();
    private final List<MovimientoPuntos> movimientos = new ArrayList<>();

    /** RP-4: saldo = puntos disponibles de los lotes que no han vencido. */
    public int getSaldo(LocalDate hoy) {
        return lotes.stream()
                .filter(l -> !l.estaVencido(hoy))
                .mapToInt(LotePuntos::getDisponibles)
                .sum();
    }

    /** RN-11: cada acumulación es un lote nuevo con su fecha. */
    public void acumular(int puntos, LocalDate fecha, String codigoCompra, String descripcion) {
        if (puntos <= 0) {
            return;
        }
        lotes.add(new LotePuntos(puntos, fecha, codigoCompra));
        movimientos.add(new MovimientoPuntos(fecha, TipoMovimientoPuntos.ACUMULACION, puntos, descripcion));
    }

    /**
     * RN-12: verifica el saldo vigente y descuenta primero de los lotes
     * que vencen más pronto. Nunca toca lotes vencidos.
     */
    public void redimir(int puntos, LocalDate hoy, String descripcion) {
        if (puntos <= 0) {
            throw new DatoInvalidoException("La cantidad de puntos a redimir debe ser mayor que 0.");
        }
        int saldo = getSaldo(hoy);
        if (saldo < puntos) {
            throw new PuntosInsuficientesException("Puntos insuficientes: se necesitan " + puntos
                    + " y tiene " + saldo + " puntos vigentes.");
        }
        List<LotePuntos> vigentes = lotes.stream()
                .filter(l -> !l.estaVencido(hoy) && l.getDisponibles() > 0)
                .sorted(Comparator.comparing(LotePuntos::getFechaVencimiento))
                .toList();
        int pendiente = puntos;
        for (LotePuntos lote : vigentes) {
            if (pendiente == 0) {
                break;
            }
            int usar = Math.min(pendiente, lote.getDisponibles());
            lote.consumir(usar);
            pendiente -= usar;
        }
        movimientos.add(new MovimientoPuntos(hoy, TipoMovimientoPuntos.REDENCION, puntos, descripcion));
    }

    /** RN-23: al reembolsar se anulan los puntos que generó esa compra y sigan disponibles. */
    public int anularPuntosDeCompra(String codigoCompra, LocalDate hoy, String descripcion) {
        int anulados = 0;
        for (LotePuntos lote : lotes) {
            if (codigoCompra.equals(lote.getCodigoCompra()) && !lote.estaVencido(hoy)) {
                anulados += lote.anular();
            }
        }
        if (anulados > 0) {
            movimientos.add(new MovimientoPuntos(hoy, TipoMovimientoPuntos.ANULACION, anulados, descripcion));
        }
        return anulados;
    }

    /** RN-24: si el cine cancela una función pagada con puntos, se devuelven en un lote nuevo. */
    public void devolver(int puntos, LocalDate hoy, String codigoCompra, String descripcion) {
        if (puntos <= 0) {
            return;
        }
        lotes.add(new LotePuntos(puntos, hoy, codigoCompra + "-DEV"));
        movimientos.add(new MovimientoPuntos(hoy, TipoMovimientoPuntos.DEVOLUCION, puntos, descripcion));
    }

    public List<LotePuntos> getLotes() {
        return lotes;
    }

    public List<MovimientoPuntos> getMovimientos() {
        return movimientos;
    }
}
