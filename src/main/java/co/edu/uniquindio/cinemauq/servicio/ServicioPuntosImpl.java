package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.excepciones.PuntosInsuficientesException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.ReglaPuntos;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación del programa de puntos. Usa ReglaPuntos (1 punto por $100; redimir = precio / 10)
 * y CuentaPuntos (lotes, vencimiento y orden de uso).
 */
public class ServicioPuntosImpl implements ServicioPuntos {

    @Override
    public int calcularPuntosNecesarios(List<? extends Vendible> vendibles) {
        int total = 0;
        for (Vendible vendible : vendibles) {
            total += ReglaPuntos.puntosNecesarios(vendible.getPrecio());
        }
        return total;
    }

    @Override
    public int consultarSaldo(Cliente cliente) {
        return cliente.getCuentaPuntos().getSaldo(LocalDate.now());
    }

    /** RN-12: se verifica el saldo de puntos vigentes antes de redimir. */
    @Override
    public void verificarPuntos(Cliente cliente, int puntos) {
        int saldo = consultarSaldo(cliente);
        if (saldo < puntos) {
            throw new PuntosInsuficientesException("Puntos insuficientes: se necesitan " + puntos
                    + " y tiene " + saldo + " puntos vigentes.");
        }
    }

    /** RP-1 y RP-2: solo las compras pagadas con tarjeta acumulan puntos. */
    @Override
    public void acumular(Compra compra) {
        if (compra.getMetodoPago() != MetodoPago.TARJETA) {
            return;
        }
        int puntos = ReglaPuntos.puntosGanados(compra.getValorPagado());
        compra.setPuntosGanados(puntos);
        compra.getCliente().getCuentaPuntos().acumular(puntos, LocalDate.now(), compra.getCodigo(),
                "Compra " + compra.getCodigo());
    }

    @Override
    public void redimir(Compra compra) {
        compra.getCliente().getCuentaPuntos().redimir(compra.getPuntosUsados(), LocalDate.now(),
                "Redención en compra " + compra.getCodigo());
    }

    /** RN-23 y RN-24: anula los puntos ganados o devuelve los puntos usados. */
    @Override
    public void revertir(Compra compra) {
        if (compra.getMetodoPago() == MetodoPago.TARJETA) {
            compra.getCliente().getCuentaPuntos().anularPuntosDeCompra(compra.getCodigo(), LocalDate.now(),
                    "Anulación por reembolso " + compra.getCodigo());
        } else {
            compra.getCliente().getCuentaPuntos().devolver(compra.getPuntosUsados(), LocalDate.now(),
                    compra.getCodigo(), "Devolución por función cancelada " + compra.getCodigo());
        }
    }
}
