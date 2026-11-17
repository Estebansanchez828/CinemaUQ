package co.edu.uniquindio.cinemauq.controlador;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.LotePuntos;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Movimiento;
import co.edu.uniquindio.cinemauq.modelo.MovimientoPuntos;
import co.edu.uniquindio.cinemauq.modelo.Notificacion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.servicio.ServicioPuntos;
import co.edu.uniquindio.cinemauq.servicio.ServicioTarjeta;
import co.edu.uniquindio.cinemauq.servicio.Sistema;
import co.edu.uniquindio.cinemauq.servicio.fachada.Cotizacion;
import co.edu.uniquindio.cinemauq.servicio.fachada.FachadaCompra;
import co.edu.uniquindio.cinemauq.util.Conversor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controlador (MVC) de las pantallas del cliente. La vista no conoce los servicios:
 * le pide todo a este controlador, que usa la FachadaCompra y los servicios.
 */
public class ClienteControlador {

    private final Cliente cliente;
    private final RepositorioCinema repositorio;
    private final FachadaCompra fachada;
    private final ServicioTarjeta servicioTarjeta;
    private final ServicioPuntos servicioPuntos;

    public ClienteControlador(Sistema sistema, Cliente cliente) {
        this.cliente = cliente;
        this.repositorio = sistema.getRepositorio();
        this.fachada = sistema.getFachadaCompra();
        this.servicioTarjeta = sistema.getServicioTarjeta();
        this.servicioPuntos = sistema.getServicioPuntos();
    }

    public Cliente getCliente() {
        return cliente;
    }

    // ----- Cartelera y compra -----

    /** RF-003: películas con al menos una función a la venta. */
    public List<Pelicula> cartelera() {
        return repositorio.getPeliculas().stream()
                .filter(p -> !funcionesDisponibles(p).isEmpty())
                .toList();
    }

    public List<Funcion> funcionesDisponibles(Pelicula pelicula) {
        LocalDateTime ahora = LocalDateTime.now();
        return repositorio.getFunciones().stream()
                .filter(f -> f.getPelicula() == pelicula && f.estaDisponibleParaVenta(ahora))
                .sorted(Comparator.comparing(Funcion::getInicio))
                .toList();
    }

    public List<ItemConfiteria> confiteria() {
        return repositorio.getConfiteria();
    }

    public Cotizacion cotizar(Funcion funcion, List<Asiento> asientos, List<ItemConfiteria> items) {
        return fachada.cotizar(funcion, asientos, items);
    }

    public Compra comprar(Funcion funcion, List<Asiento> asientos, List<ItemConfiteria> items, MetodoPago metodo) {
        return fachada.comprar(cliente, funcion, asientos, items, metodo);
    }

    // ----- Compras -----

    public List<Compra> misCompras() {
        List<Compra> compras = new ArrayList<>(fachada.historial(cliente));
        compras.sort(Comparator.comparing(Compra::getFecha).reversed());
        return compras;
    }

    public Reembolso cancelar(Compra compra) {
        return fachada.cancelarCompra(compra);
    }

    // ----- Tarjeta -----

    public List<Movimiento> movimientos() {
        List<Movimiento> lista = new ArrayList<>(cliente.getTarjeta().getMovimientos());
        Collections.reverse(lista);
        return lista;
    }

    public SolicitudRecarga solicitarRecarga(String valorTexto) {
        return servicioTarjeta.solicitarRecarga(cliente, Conversor.aDouble(valorTexto, "valor"));
    }

    public List<SolicitudRecarga> misSolicitudes() {
        List<SolicitudRecarga> lista = new ArrayList<>(servicioTarjeta.solicitudesDe(cliente));
        Collections.reverse(lista);
        return lista;
    }

    // ----- Puntos y notificaciones -----

    public int saldoPuntos() {
        return servicioPuntos.consultarSaldo(cliente);
    }

    public List<LotePuntos> lotes() {
        List<LotePuntos> lista = new ArrayList<>(cliente.getCuentaPuntos().getLotes());
        lista.sort(Comparator.comparing(LotePuntos::getFechaVencimiento));
        return lista;
    }

    public List<MovimientoPuntos> movimientosPuntos() {
        List<MovimientoPuntos> lista = new ArrayList<>(cliente.getCuentaPuntos().getMovimientos());
        Collections.reverse(lista);
        return lista;
    }

    public List<Notificacion> notificaciones() {
        return cliente.getNotificaciones();
    }
}
