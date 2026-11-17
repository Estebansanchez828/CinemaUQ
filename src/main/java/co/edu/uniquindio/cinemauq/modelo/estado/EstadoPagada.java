package co.edu.uniquindio.cinemauq.modelo.estado;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón State – rol: Estado concreto. Compra pagada: solo se puede cancelar. */
public class EstadoPagada implements EstadoCompra {

    @Override
    public String getNombre() {
        return "Pagada";
    }

    @Override
    public void pagar(Compra compra) {
        throw new OperacionNoPermitidaException("La compra " + compra.getCodigo() + " ya está pagada.");
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado(new EstadoCancelada());
    }

    @Override
    public void reembolsar(Compra compra) {
        throw new OperacionNoPermitidaException("Primero se debe cancelar la compra " + compra.getCodigo() + ".");
    }
}
