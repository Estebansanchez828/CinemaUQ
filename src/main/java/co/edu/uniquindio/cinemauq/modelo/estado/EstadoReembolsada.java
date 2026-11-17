package co.edu.uniquindio.cinemauq.modelo.estado;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.excepciones.ReembolsoDuplicadoException;
import co.edu.uniquindio.cinemauq.modelo.Compra;

/**
 * Patrón State – rol: Estado concreto. Estado final: impide reembolsar dos veces (RN-10).
 */
public class EstadoReembolsada implements EstadoCompra {

    @Override
    public String getNombre() {
        return "Reembolsada";
    }

    @Override
    public void pagar(Compra compra) {
        throw new OperacionNoPermitidaException("La compra " + compra.getCodigo() + " ya fue reembolsada.");
    }

    @Override
    public void cancelar(Compra compra) {
        throw new ReembolsoDuplicadoException("La compra " + compra.getCodigo() + " ya fue reembolsada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        throw new ReembolsoDuplicadoException("La compra " + compra.getCodigo() + " ya fue reembolsada.");
    }
}
