package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;

import java.util.List;

/**
 * Operaciones del cliente con su tarjeta virtual. La recarga NO está aquí:
 * solo la hace el administrador a través del Proxy (RN-05). SOLID – ISP.
 */
public interface ServicioTarjeta {

    void verificarPago(Cliente cliente, double valor);

    void cobrar(Compra compra);

    void abonarReembolso(Compra compra, double valor, int porcentaje);

    SolicitudRecarga solicitarRecarga(Cliente cliente, double valor);

    List<SolicitudRecarga> solicitudesDe(Cliente cliente);
}
