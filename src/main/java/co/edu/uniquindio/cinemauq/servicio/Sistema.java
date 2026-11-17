package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.DatosIniciales;
import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.servicio.admin.ProxyAdmin;
import co.edu.uniquindio.cinemauq.servicio.admin.ServicioAdmin;
import co.edu.uniquindio.cinemauq.servicio.evento.NotificadorCompras;
import co.edu.uniquindio.cinemauq.servicio.evento.ObservadorEstadisticas;
import co.edu.uniquindio.cinemauq.servicio.evento.ObservadorNotificaciones;
import co.edu.uniquindio.cinemauq.servicio.evento.ObservadorPuntos;
import co.edu.uniquindio.cinemauq.servicio.fachada.FachadaCompra;

/**
 * Ensambla la aplicación: crea cada servicio una sola vez y le pasa sus dependencias
 * por el constructor (SOLID – DIP). También suscribe los observadores.
 */
public class Sistema {

    private final RepositorioCinema repositorio;
    private final ServicioAutenticacion servicioAutenticacion;
    private final ServicioTarjeta servicioTarjeta;
    private final ServicioPuntos servicioPuntos;
    private final ServicioPromociones servicioPromociones;
    private final ServicioAdmin servicioAdmin;
    private final FachadaCompra fachadaCompra;
    private final ServicioEstadisticas servicioEstadisticas;
    private final ObservadorEstadisticas observadorEstadisticas = new ObservadorEstadisticas();

    public Sistema() {
        DatosIniciales.cargar();
        repositorio = RepositorioCinema.getInstancia();                     // Singleton
        servicioAutenticacion = new ServicioAutenticacionImpl(repositorio);
        ServicioAsientos servicioAsientos = new ServicioAsientosImpl();
        servicioTarjeta = new ServicioTarjetaImpl(repositorio);
        servicioPuntos = new ServicioPuntosImpl();
        servicioPromociones = new ServicioPromocionesImpl(repositorio);

        NotificadorCompras notificador = new NotificadorCompras();          // Observer: sujeto
        notificador.suscribir(new ObservadorPuntos(servicioPuntos));
        notificador.suscribir(observadorEstadisticas);
        notificador.suscribir(new ObservadorNotificaciones());

        ServicioReembolso servicioReembolso = new ServicioReembolsoImpl(repositorio, servicioTarjeta,
                servicioAsientos, notificador);
        servicioAdmin = new ServicioAdmin(repositorio, servicioReembolso);
        fachadaCompra = new FachadaCompra(servicioAsientos, servicioTarjeta, servicioPuntos,
                servicioPromociones, servicioReembolso, notificador, repositorio);
        servicioEstadisticas = new ServicioEstadisticasImpl(repositorio);
    }

    /** Patrón Proxy: toda operación de administración pasa por el proxy del usuario en sesión. */
    public ProxyAdmin administracion(Usuario usuario) {
        return new ProxyAdmin(usuario, servicioAdmin);
    }

    public RepositorioCinema getRepositorio() {
        return repositorio;
    }

    public ServicioAutenticacion getServicioAutenticacion() {
        return servicioAutenticacion;
    }

    public ServicioTarjeta getServicioTarjeta() {
        return servicioTarjeta;
    }

    public ServicioPuntos getServicioPuntos() {
        return servicioPuntos;
    }

    public ServicioPromociones getServicioPromociones() {
        return servicioPromociones;
    }

    public FachadaCompra getFachadaCompra() {
        return fachadaCompra;
    }

    public ServicioEstadisticas getServicioEstadisticas() {
        return servicioEstadisticas;
    }

    public ObservadorEstadisticas getObservadorEstadisticas() {
        return observadorEstadisticas;
    }
}
