package co.edu.uniquindio.cinemauq.servicio.evento;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Observer – rol: Sujeto concreto. Al pagar, reembolsar o cancelar una función
 * avisa a todos los suscriptores sin conocer qué hace cada uno.
 */
public class NotificadorCompras implements SujetoCompra {

    private final List<ObservadorCompra> observadores = new ArrayList<>();

    @Override
    public void suscribir(ObservadorCompra observador) {
        observadores.add(observador);
    }

    @Override
    public void desuscribir(ObservadorCompra observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(EventoCompra evento) {
        for (ObservadorCompra observador : observadores) {
            observador.actualizar(evento);
        }
    }
}
