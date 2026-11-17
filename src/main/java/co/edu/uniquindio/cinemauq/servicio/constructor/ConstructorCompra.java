package co.edu.uniquindio.cinemauq.servicio.constructor;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;

import java.time.LocalDateTime;

/**
 * Patrón Builder – rol: Builder (interfaz con los pasos para armar una Compra).
 */
public interface ConstructorCompra {

    ConstructorCompra paraCliente(Cliente cliente);

    ConstructorCompra conFuncion(Funcion funcion);

    ConstructorCompra agregarEntrada(Entrada entrada);

    ConstructorCompra agregarItem(ItemConfiteria item);

    ConstructorCompra conDescuento(double descuento);

    ConstructorCompra conMetodoPago(MetodoPago metodoPago, int puntosUsados);

    ConstructorCompra enFecha(LocalDateTime fecha);

    Compra construir();
}
