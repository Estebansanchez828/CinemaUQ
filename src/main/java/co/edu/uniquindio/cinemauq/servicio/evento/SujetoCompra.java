package co.edu.uniquindio.cinemauq.servicio.evento;

/** Patrón Observer – rol: Sujeto (permite suscribirse y avisa a todos). */
public interface SujetoCompra {

    void suscribir(ObservadorCompra observador);

    void desuscribir(ObservadorCompra observador);

    void notificar(EventoCompra evento);
}
