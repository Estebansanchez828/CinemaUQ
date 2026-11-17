package co.edu.uniquindio.cinemauq.controlador;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.servicio.ServicioEstadisticas;
import co.edu.uniquindio.cinemauq.servicio.Sistema;
import co.edu.uniquindio.cinemauq.servicio.admin.AdministracionCatalogo;
import co.edu.uniquindio.cinemauq.servicio.admin.AdministracionOperaciones;
import co.edu.uniquindio.cinemauq.servicio.evento.ObservadorEstadisticas;
import co.edu.uniquindio.cinemauq.util.Conversor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Controlador (MVC) del administrador. Todas las operaciones de administración
 * pasan por el ProxyAdmin (Patrón Proxy), visto aquí como sus dos interfaces (SOLID – ISP y DIP).
 */
public class AdminControlador {

    private final RepositorioCinema repositorio;
    private final AdministracionCatalogo catalogo;
    private final AdministracionOperaciones operaciones;
    private final ServicioEstadisticas estadisticas;
    private final ObservadorEstadisticas actividad;

    public AdminControlador(Sistema sistema, Usuario usuario) {
        this.repositorio = sistema.getRepositorio();
        this.catalogo = sistema.administracion(usuario);
        this.operaciones = sistema.administracion(usuario);
        this.estadisticas = sistema.getServicioEstadisticas();
        this.actividad = sistema.getObservadorEstadisticas();
    }

    // ----- Listados -----

    public List<Pelicula> peliculas() {
        return repositorio.getPeliculas();
    }

    public List<Sala> salas() {
        return repositorio.getSalas();
    }

    public List<Funcion> funciones() {
        List<Funcion> lista = new ArrayList<>(repositorio.getFunciones());
        lista.sort(Comparator.comparing(Funcion::getInicio).reversed());
        return lista;
    }

    public List<ItemConfiteria> confiteria() {
        return repositorio.getConfiteria();
    }

    public List<Promocion> promociones() {
        return repositorio.getPromociones();
    }

    public List<Cliente> clientes() {
        return operaciones.listarClientes();
    }

    public List<Compra> compras() {
        List<Compra> lista = new ArrayList<>(operaciones.listarCompras());
        lista.sort(Comparator.comparing(Compra::getFecha).reversed());
        return lista;
    }

    public List<SolicitudRecarga> solicitudesPendientes() {
        return operaciones.solicitudesPendientes();
    }

    public List<SolicitudRecarga> todasLasSolicitudes() {
        List<SolicitudRecarga> lista = new ArrayList<>(repositorio.getSolicitudes());
        lista.sort(Comparator.comparing(SolicitudRecarga::getFecha).reversed());
        return lista;
    }

    public int puntosVigentes(Cliente cliente) {
        return cliente.getCuentaPuntos().getSaldo(LocalDate.now());
    }

    // ----- Catálogo -----

    public void crearPelicula(String titulo, String genero, String duracion, String clasificacion) {
        catalogo.crearPelicula(titulo, genero, Conversor.aEntero(duracion, "duración"), clasificacion);
    }

    public void eliminarPelicula(Pelicula pelicula) {
        catalogo.eliminarPelicula(requerido(pelicula, "una película"));
    }

    public void crearSala(String nombre, String filas, String columnas) {
        catalogo.crearSala(nombre, Conversor.aEntero(filas, "filas"), Conversor.aEntero(columnas, "columnas"));
    }

    public void eliminarSala(Sala sala) {
        catalogo.eliminarSala(requerido(sala, "una sala"));
    }

    public void cambiarTipoAsiento(Sala sala, String codigo, TipoAsiento tipo) {
        catalogo.cambiarTipoAsiento(requerido(sala, "una sala"), codigo, requerido(tipo, "un tipo de asiento"));
    }

    public void crearFuncion(Pelicula pelicula, Sala sala, LocalDate fecha, String hora, String precio) {
        if (fecha == null) {
            throw new DatoInvalidoException("Seleccione la fecha de la función.");
        }
        LocalTime horaInicio;
        try {
            horaInicio = LocalTime.parse(hora.trim());
        } catch (DateTimeParseException | NullPointerException e) {
            throw new DatoInvalidoException("La hora debe tener el formato HH:mm (ejemplo 19:30).");
        }
        catalogo.crearFuncion(pelicula, sala, LocalDateTime.of(fecha, horaInicio), Conversor.aDouble(precio, "precio"));
    }

    public void eliminarFuncion(Funcion funcion) {
        catalogo.eliminarFuncion(requerido(funcion, "una función"));
    }

    public List<Reembolso> cancelarFuncion(Funcion funcion) {
        return operaciones.cancelarFuncion(requerido(funcion, "una función"));
    }

    public void crearProducto(String nombre, String categoria, String precio) {
        catalogo.crearProducto(nombre, categoria, Conversor.aDouble(precio, "precio"));
    }

    public void crearCombo(String nombre, String descuento, List<ItemConfiteria> items) {
        catalogo.crearCombo(nombre, Conversor.aDouble(descuento, "descuento"), items);
    }

    public void eliminarItem(ItemConfiteria item) {
        catalogo.eliminarItemConfiteria(requerido(item, "un ítem"));
    }

    public void crearPromocion(String nombre, TipoPromocion tipo, String valor, AlcancePromocion alcance) {
        catalogo.crearPromocion(nombre, tipo, Conversor.aDouble(valor, "valor"), alcance);
    }

    public void alternarPromocion(Promocion promocion) {
        requerido(promocion, "una promoción");
        catalogo.cambiarEstadoPromocion(promocion, !promocion.isActiva());
    }

    public void eliminarPromocion(Promocion promocion) {
        catalogo.eliminarPromocion(requerido(promocion, "una promoción"));
    }

    // ----- Operaciones -----

    public void aprobar(SolicitudRecarga solicitud) {
        operaciones.aprobarSolicitud(requerido(solicitud, "una solicitud"));
    }

    public void rechazar(SolicitudRecarga solicitud) {
        operaciones.rechazarSolicitud(requerido(solicitud, "una solicitud"));
    }

    public void recargar(Cliente cliente, String valor) {
        operaciones.recargarTarjeta(requerido(cliente, "un cliente"), Conversor.aDouble(valor, "valor"));
    }

    public void alternarTarjeta(Cliente cliente) {
        requerido(cliente, "un cliente");
        operaciones.cambiarEstadoTarjeta(cliente, !cliente.getTarjeta().isActiva());
    }

    // ----- Estadísticas -----

    public ServicioEstadisticas estadisticas() {
        return estadisticas;
    }

    public ObservadorEstadisticas actividadSesion() {
        return actividad;
    }

    private <T> T requerido(T valor, String que) {
        if (valor == null) {
            throw new DatoInvalidoException("Seleccione " + que + ".");
        }
        return valor;
    }
}
