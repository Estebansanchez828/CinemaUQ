package co.edu.uniquindio.cinemauq.servicio.fachada;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.ReglaPuntos;
import co.edu.uniquindio.cinemauq.modelo.Vendible;
import co.edu.uniquindio.cinemauq.servicio.ServicioAsientos;
import co.edu.uniquindio.cinemauq.servicio.ServicioPromociones;
import co.edu.uniquindio.cinemauq.servicio.ServicioPuntos;
import co.edu.uniquindio.cinemauq.servicio.ServicioReembolso;
import co.edu.uniquindio.cinemauq.servicio.ServicioTarjeta;
import co.edu.uniquindio.cinemauq.servicio.constructor.ConstructorCompraEstandar;
import co.edu.uniquindio.cinemauq.servicio.constructor.DirectorCompra;
import co.edu.uniquindio.cinemauq.servicio.evento.EventoCompra;
import co.edu.uniquindio.cinemauq.servicio.evento.SujetoCompra;
import co.edu.uniquindio.cinemauq.servicio.evento.TipoEvento;
import co.edu.uniquindio.cinemauq.servicio.fabrica.CreadorEntrada;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fachada de compra: la vista llama a un solo método y la fachada coordina
 * asientos, Factory Method, Decorator, Builder, tarjeta, puntos, State y Observer.
 * Patrón Facade – rol: Fachada.
 * SOLID – DIP: recibe todos los servicios como interfaces por el constructor.
 */
public class FachadaCompra {

    private final ServicioAsientos servicioAsientos;
    private final ServicioTarjeta servicioTarjeta;
    private final ServicioPuntos servicioPuntos;
    private final ServicioPromociones servicioPromociones;
    private final ServicioReembolso servicioReembolso;
    private final SujetoCompra notificador;
    private final RepositorioCinema repositorio;
    private final DirectorCompra director = new DirectorCompra();

    public FachadaCompra(ServicioAsientos servicioAsientos, ServicioTarjeta servicioTarjeta,
                         ServicioPuntos servicioPuntos, ServicioPromociones servicioPromociones,
                         ServicioReembolso servicioReembolso, SujetoCompra notificador,
                         RepositorioCinema repositorio) {
        this.servicioAsientos = servicioAsientos;
        this.servicioTarjeta = servicioTarjeta;
        this.servicioPuntos = servicioPuntos;
        this.servicioPromociones = servicioPromociones;
        this.servicioReembolso = servicioReembolso;
        this.notificador = notificador;
        this.repositorio = repositorio;
    }

    /** RF-006: muestra precios con promociones y los puntos que costaría redimir. */
    public Cotizacion cotizar(Funcion funcion, List<Asiento> asientos, List<ItemConfiteria> items) {
        List<Vendible> vendibles = new ArrayList<>(crearEntradas(funcion, asientos));
        vendibles.addAll(items);
        if (vendibles.isEmpty()) {
            throw new DatoInvalidoException("Seleccione al menos un asiento o un producto.");
        }
        List<String> detalle = new ArrayList<>();
        double subtotal = 0;
        for (Vendible vendible : vendibles) {
            Vendible conPromocion = servicioPromociones.aplicarPromociones(vendible);   // Decorator
            subtotal += vendible.getPrecio();
            detalle.add(conPromocion.getNombre() + ": " + Formato.dinero(vendible.getPrecio())
                    + " -> " + Formato.dinero(conPromocion.getPrecio())
                    + "  (o " + ReglaPuntos.puntosNecesarios(vendible.getPrecio()) + " puntos)");
        }
        return new Cotizacion(detalle, subtotal, servicioPromociones.calcularDescuento(vendibles),
                servicioPuntos.calcularPuntosNecesarios(vendibles));
    }

    /** RF-007 y RF-013: compra completa con tarjeta o con puntos, en un solo llamado. */
    public Compra comprar(Cliente cliente, Funcion funcion, List<Asiento> asientos,
                          List<ItemConfiteria> items, MetodoPago metodoPago) {
        Funcion funcionCompra = asientos.isEmpty() ? null : funcion;
        if (funcionCompra != null) {
            servicioAsientos.verificarDisponibles(funcionCompra, asientos);          // RN-02, RN-15
        }
        List<Entrada> entradas = crearEntradas(funcionCompra, asientos);             // Factory Method
        List<Vendible> vendibles = new ArrayList<>(entradas);
        vendibles.addAll(items);
        double subtotal = vendibles.stream().mapToDouble(Vendible::getPrecio).sum();
        LocalDateTime ahora = LocalDateTime.now();
        Compra compra;
        if (metodoPago == MetodoPago.TARJETA) {
            double descuento = servicioPromociones.calcularDescuento(vendibles);       // Decorator
            servicioTarjeta.verificarPago(cliente, subtotal - descuento);             // RN-03
            compra = director.construirCompraConTarjeta(new ConstructorCompraEstandar(), cliente,
                    funcionCompra, entradas, items, descuento, ahora);                 // Builder
            ocuparAsientos(funcionCompra, asientos);
            servicioTarjeta.cobrar(compra);                                           // RN-04
        } else {
            int puntos = servicioPuntos.calcularPuntosNecesarios(vendibles);
            servicioPuntos.verificarPuntos(cliente, puntos);                          // RN-12
            compra = director.construirRedencion(new ConstructorCompraEstandar(), cliente,
                    funcionCompra, entradas, items, puntos, ahora);                    // Builder
            ocuparAsientos(funcionCompra, asientos);
            servicioPuntos.redimir(compra);
        }
        compra.pagar();                                                               // State
        repositorio.getCompras().add(compra);
        notificador.notificar(new EventoCompra(TipoEvento.COMPRA_PAGADA, compra, compra.getValorPagado())); // Observer
        return compra;
    }

    /** RF-011: cancelación con reembolso según la política que corresponda (Strategy). */
    public Reembolso cancelarCompra(Compra compra) {
        return servicioReembolso.cancelarCompra(compra);
    }

    public List<Compra> historial(Cliente cliente) {
        return repositorio.comprasDe(cliente);
    }

    private List<Entrada> crearEntradas(Funcion funcion, List<Asiento> asientos) {
        List<Entrada> entradas = new ArrayList<>();
        if (asientos.isEmpty()) {
            return entradas;
        }
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione una función.");
        }
        for (Asiento asiento : asientos) {
            entradas.add(CreadorEntrada.segunTipo(asiento.getTipo()).emitir(funcion, asiento));
        }
        return entradas;
    }

    private void ocuparAsientos(Funcion funcion, List<Asiento> asientos) {
        if (funcion != null) {
            servicioAsientos.ocupar(funcion, asientos);
        }
    }
}
