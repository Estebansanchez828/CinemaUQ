package co.edu.uniquindio.cinemauq.servicio.admin;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;

import java.util.List;

/**
 * Operaciones del administrador con clientes, recargas y funciones (RF-021 a RF-024).
 * Patrón Proxy – rol: Sujeto. SOLID – ISP.
 */
public interface AdministracionOperaciones {

    List<Cliente> listarClientes();

    List<Compra> listarCompras();

    List<SolicitudRecarga> solicitudesPendientes();

    void aprobarSolicitud(SolicitudRecarga solicitud);

    void rechazarSolicitud(SolicitudRecarga solicitud);

    void recargarTarjeta(Cliente cliente, double valor);

    void cambiarEstadoTarjeta(Cliente cliente, boolean activa);

    List<Reembolso> cancelarFuncion(Funcion funcion);
}
