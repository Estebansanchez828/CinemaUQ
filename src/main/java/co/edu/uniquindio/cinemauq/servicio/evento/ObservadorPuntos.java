package co.edu.uniquindio.cinemauq.servicio.evento;

import co.edu.uniquindio.cinemauq.servicio.ServicioPuntos;

/**
 * Patrón Observer – rol: Observador concreto.
 * Pago con tarjeta: acumula puntos. Reembolso: anula los puntos de esa compra
 * o devuelve los puntos usados si la función la canceló el cine.
 */
public class ObservadorPuntos implements ObservadorCompra {

    private final ServicioPuntos servicioPuntos;

    public ObservadorPuntos(ServicioPuntos servicioPuntos) {
        this.servicioPuntos = servicioPuntos;
    }

    @Override
    public void actualizar(EventoCompra evento) {
        switch (evento.getTipo()) {
            case COMPRA_PAGADA -> servicioPuntos.acumular(evento.getCompra());
            case COMPRA_REEMBOLSADA, FUNCION_CANCELADA -> servicioPuntos.revertir(evento.getCompra());
        }
    }
}
