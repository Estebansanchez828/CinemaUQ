package co.edu.uniquindio.cinemauq.servicio.evento;

import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.util.Formato;

/** Patrón Observer – rol: Observador concreto. Deja un mensaje al cliente (RF-014). */
public class ObservadorNotificaciones implements ObservadorCompra {

    @Override
    public void actualizar(EventoCompra evento) {
        Compra compra = evento.getCompra();
        String mensaje = switch (evento.getTipo()) {
            case COMPRA_PAGADA -> compra.getMetodoPago() == MetodoPago.TARJETA
                    ? "Pago aprobado: compra " + compra.getCodigo() + " por " + Formato.dinero(compra.getTotal())
                      + ". Ganó " + compra.getPuntosGanados() + " puntos."
                    : "Redención aprobada: compra " + compra.getCodigo() + " con " + compra.getPuntosUsados()
                      + " puntos.";
            case COMPRA_REEMBOLSADA -> "Compra " + compra.getCodigo() + " cancelada. Reembolso de "
                    + Formato.dinero(evento.getValor()) + " a su tarjeta.";
            case FUNCION_CANCELADA -> "El cine canceló la función " + compra.getFuncion() + ". Se reembolsó el 100 %: "
                    + (compra.getMetodoPago() == MetodoPago.TARJETA ? Formato.dinero(evento.getValor())
                    : compra.getPuntosUsados() + " puntos") + ".";
        };
        compra.getCliente().agregarNotificacion(mensaje);
    }
}
