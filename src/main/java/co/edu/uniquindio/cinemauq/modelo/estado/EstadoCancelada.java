package co.edu.uniquindio.cinemauq.modelo.estado;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón State – rol: Estado concreto. Compra cancelada, lista para reembolsar. */
public class EstadoCancelada implements EstadoCompra {

    @Override
    public String getNombre() {
        return "Cancelada";
    }

    @Override
    public void pagar(Compra compra) {
        throw new OperacionNoPermitidaException("La compra " + compra.getCodigo() + " está cancelada.");
    }

    @Override
    public void cancelar(Compra compra) {
        throw new OperacionNoPermitidaException("La compra " + compra.getCodigo() + " ya está cancelada.");
    }

    @Override
    public void reembolsar(Compra compra) {
        compra.setEstado(new EstadoReembolsada());
    }
}
