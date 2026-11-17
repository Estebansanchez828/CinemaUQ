package co.edu.uniquindio.cinemauq.modelo.estado;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón State – rol: Estado concreto. Compra creada, aún sin pagar. */
public class EstadoPendiente implements EstadoCompra {

    @Override
    public String getNombre() {
        return "Pendiente";
    }

    @Override
    public void pagar(Compra compra) {
        compra.setEstado(new EstadoPagada());
    }

    @Override
    public void cancelar(Compra compra) {
        compra.setEstado(new EstadoCancelada());
    }

    @Override
    public void reembolsar(Compra compra) {
        throw new OperacionNoPermitidaException("La compra " + compra.getCodigo() + " no se ha pagado.");
    }
}
