package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;

import java.time.LocalDateTime;

/** Solicitud de recarga que hace el cliente y atiende el administrador (RN-05, RN-21). */
public class SolicitudRecarga {

    private final int id;
    private final Cliente cliente;
    private final double valor;
    private final LocalDateTime fecha;
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    public SolicitudRecarga(int id, Cliente cliente, double valor, LocalDateTime fecha) {
        this.id = id;
        this.cliente = cliente;
        this.valor = valor;
        this.fecha = fecha;
    }

    public void aprobar() {
        verificarPendiente();
        estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar() {
        verificarPendiente();
        estado = EstadoSolicitud.RECHAZADA;
    }

    private void verificarPendiente() {
        if (estado != EstadoSolicitud.PENDIENTE) {
            throw new OperacionNoPermitidaException("La solicitud " + id + " ya fue atendida.");
        }
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }
}
