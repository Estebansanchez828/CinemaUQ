package co.edu.uniquindio.cinemauq.servicio.admin;

import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Combo;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Producto;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestión del catálogo del cine (RF-015 a RF-020).
 * Patrón Proxy – rol: Sujeto. SOLID – ISP: separada de las operaciones con clientes.
 */
public interface AdministracionCatalogo {

    Pelicula crearPelicula(String titulo, String genero, int duracionMinutos, String clasificacion);

    void eliminarPelicula(Pelicula pelicula);

    Sala crearSala(String nombre, int filas, int columnas);

    void eliminarSala(Sala sala);

    void cambiarTipoAsiento(Sala sala, String codigoAsiento, TipoAsiento tipo);

    Funcion crearFuncion(Pelicula pelicula, Sala sala, LocalDateTime inicio, double precioBase);

    void eliminarFuncion(Funcion funcion);

    Producto crearProducto(String nombre, String categoria, double precio);

    Combo crearCombo(String nombre, double descuentoPorcentaje, List<ItemConfiteria> items);

    void eliminarItemConfiteria(ItemConfiteria item);

    Promocion crearPromocion(String nombre, TipoPromocion tipo, double valor, AlcancePromocion alcance);

    void cambiarEstadoPromocion(Promocion promocion, boolean activa);

    void eliminarPromocion(Promocion promocion);
}
