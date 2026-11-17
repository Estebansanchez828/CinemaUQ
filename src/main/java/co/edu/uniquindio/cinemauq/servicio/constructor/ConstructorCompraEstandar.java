package co.edu.uniquindio.cinemauq.servicio.constructor;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Patrón Builder – rol: Builder concreto.
 * Junta las partes paso a paso y valida la compra completa en construir() (RN-16).
 */
public class ConstructorCompraEstandar implements ConstructorCompra {

    private Cliente cliente;
    private Funcion funcion;
    private final List<Entrada> entradas = new ArrayList<>();
    private final List<ItemConfiteria> items = new ArrayList<>();
    private double descuento;
    private MetodoPago metodoPago = MetodoPago.TARJETA;
    private int puntosUsados;
    private LocalDateTime fecha = LocalDateTime.now();

    @Override
    public ConstructorCompra paraCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    @Override
    public ConstructorCompra conFuncion(Funcion funcion) {
        this.funcion = funcion;
        return this;
    }

    @Override
    public ConstructorCompra agregarEntrada(Entrada entrada) {
        entradas.add(entrada);
        return this;
    }

    @Override
    public ConstructorCompra agregarItem(ItemConfiteria item) {
        items.add(item);
        return this;
    }

    @Override
    public ConstructorCompra conDescuento(double descuento) {
        this.descuento = descuento;
        return this;
    }

    @Override
    public ConstructorCompra conMetodoPago(MetodoPago metodoPago, int puntosUsados) {
        this.metodoPago = metodoPago;
        this.puntosUsados = puntosUsados;
        return this;
    }

    @Override
    public ConstructorCompra enFecha(LocalDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    @Override
    public Compra construir() {
        if (cliente == null) {
            throw new DatoInvalidoException("La compra debe tener un cliente.");
        }
        if (entradas.isEmpty() && items.isEmpty()) {
            throw new DatoInvalidoException("La compra debe tener al menos una entrada o un producto.");
        }
        Set<String> asientos = new HashSet<>();
        for (Entrada entrada : entradas) {
            if (funcion == null || entrada.getFuncion() != funcion) {
                throw new DatoInvalidoException("Todas las entradas deben ser de la función elegida.");
            }
            if (!asientos.add(entrada.getAsiento().getCodigo())) {
                throw new DatoInvalidoException("El asiento " + entrada.getAsiento() + " está repetido.");
            }
        }
        double subtotal = 0;
        List<Vendible> vendibles = new ArrayList<>(entradas);
        vendibles.addAll(items);
        for (Vendible vendible : vendibles) {
            subtotal += vendible.getPrecio();
        }
        if (descuento < 0 || descuento > subtotal) {
            throw new DatoInvalidoException("El descuento no es válido.");
        }
        String codigo = RepositorioCinema.getInstancia().siguienteCodigoCompra();
        return new Compra(codigo, cliente, funcion, entradas, items, subtotal, descuento,
                metodoPago, puntosUsados, fecha);
    }
}
