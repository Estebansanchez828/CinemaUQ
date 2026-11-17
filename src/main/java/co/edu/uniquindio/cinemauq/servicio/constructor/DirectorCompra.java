package co.edu.uniquindio.cinemauq.servicio.constructor;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Patrón Builder – rol: Director.
 * Conoce las dos "recetas" de compra: pagada con tarjeta (con promociones)
 * y redención con puntos (sin promociones, RN-20).
 */
public class DirectorCompra {

    public Compra construirCompraConTarjeta(ConstructorCompra constructor, Cliente cliente, Funcion funcion,
                                           List<Entrada> entradas, List<ItemConfiteria> items,
                                           double descuento, LocalDateTime fecha) {
        armarBase(constructor, cliente, funcion, entradas, items, fecha);
        return constructor.conDescuento(descuento)
                .conMetodoPago(MetodoPago.TARJETA, 0)
                .construir();
    }

    public Compra construirRedencion(ConstructorCompra constructor, Cliente cliente, Funcion funcion,
                                     List<Entrada> entradas, List<ItemConfiteria> items,
                                     int puntos, LocalDateTime fecha) {
        armarBase(constructor, cliente, funcion, entradas, items, fecha);
        return constructor.conDescuento(0)
                .conMetodoPago(MetodoPago.PUNTOS, puntos)
                .construir();
    }

    private void armarBase(ConstructorCompra constructor, Cliente cliente, Funcion funcion,
                           List<Entrada> entradas, List<ItemConfiteria> items, LocalDateTime fecha) {
        constructor.paraCliente(cliente).conFuncion(funcion).enFecha(fecha);
        entradas.forEach(constructor::agregarEntrada);
        items.forEach(constructor::agregarItem);
    }
}
