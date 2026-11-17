package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.modelo.estado.EstadoCompra;
import co.edu.uniquindio.cinemauq.modelo.estado.EstadoPagada;
import co.edu.uniquindio.cinemauq.modelo.estado.EstadoPendiente;
import co.edu.uniquindio.cinemauq.modelo.estado.EstadoReembolsada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Compra de entradas y/o confitería.
 * Patrón Builder – rol: Producto (se arma con ConstructorCompra).
 * Patrón State – rol: Contexto (delega pagar, cancelar y reembolsar a su estado).
 */
public class Compra {

    private final String codigo;
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Entrada> entradas;
    private final List<ItemConfiteria> items;
    private final double subtotal;
    private final double descuento;
    private final MetodoPago metodoPago;
    private final int puntosUsados;
    private final LocalDateTime fecha;
    private EstadoCompra estado = new EstadoPendiente();
    private int puntosGanados;
    private Reembolso reembolso;

    public Compra(String codigo, Cliente cliente, Funcion funcion, List<Entrada> entradas,
                  List<ItemConfiteria> items, double subtotal, double descuento,
                  MetodoPago metodoPago, int puntosUsados, LocalDateTime fecha) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.entradas = new ArrayList<>(entradas);
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.metodoPago = metodoPago;
        this.puntosUsados = puntosUsados;
        this.fecha = fecha;
    }

    // ----- Patrón State: la compra delega en su estado actual -----

    public void pagar() {
        estado.pagar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public void reembolsar(Reembolso reembolso) {
        estado.reembolsar(this);
        this.reembolso = reembolso;
    }

    /** Lo usan los estados para pasar al siguiente. */
    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public String getNombreEstado() {
        return estado.getNombre();
    }

    public boolean estaPagada() {
        return estado instanceof EstadoPagada;
    }

    /** Pagada o reembolsada: la venta sí ocurrió (sirve para reportes). */
    public boolean fueVendida() {
        return estado instanceof EstadoPagada || estado instanceof EstadoReembolsada;
    }

    // ----- Datos -----

    public double getTotal() {
        return subtotal - descuento;
    }

    /** Dinero cobrado a la tarjeta (0 si se pagó con puntos). */
    public double getValorPagado() {
        return metodoPago == MetodoPago.TARJETA ? getTotal() : 0;
    }

    public String getResumen() {
        String texto = entradas.size() + " entrada(s)";
        if (!items.isEmpty()) {
            texto += " + " + items.size() + " de confitería";
        }
        return texto;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public List<ItemConfiteria> getItems() {
        return items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public int getPuntosUsados() {
        return puntosUsados;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getPuntosGanados() {
        return puntosGanados;
    }

    public void setPuntosGanados(int puntosGanados) {
        this.puntosGanados = puntosGanados;
    }

    public Reembolso getReembolso() {
        return reembolso;
    }
}
