package co.edu.uniquindio.cinemauq.servicio.admin;

import co.edu.uniquindio.cinemauq.excepciones.AccesoDenegadoException;
import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Combo;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Producto;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;
import co.edu.uniquindio.cinemauq.modelo.Usuario;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Proxy de protección: antes de cada operación de administración verifica que
 * el usuario en sesión sea administrador; si no, lanza AccesoDenegadoException (RN-05).
 * Patrón Proxy – rol: Proxy (implementa las mismas interfaces que el Sujeto real).
 */
public class ProxyAdmin implements AdministracionCatalogo, AdministracionOperaciones {

    private final Usuario usuario;
    private final ServicioAdmin real;

    public ProxyAdmin(Usuario usuario, ServicioAdmin real) {
        this.usuario = usuario;
        this.real = real;
    }

    private void verificarAcceso() {
        if (usuario == null || !usuario.esAdministrador()) {
            throw new AccesoDenegadoException("Acceso denegado: solo el administrador puede realizar esta operación.");
        }
    }

    @Override
    public Pelicula crearPelicula(String titulo, String genero, int duracionMinutos, String clasificacion) {
        verificarAcceso();
        return real.crearPelicula(titulo, genero, duracionMinutos, clasificacion);
    }

    @Override
    public void eliminarPelicula(Pelicula pelicula) {
        verificarAcceso();
        real.eliminarPelicula(pelicula);
    }

    @Override
    public Sala crearSala(String nombre, int filas, int columnas) {
        verificarAcceso();
        return real.crearSala(nombre, filas, columnas);
    }

    @Override
    public void eliminarSala(Sala sala) {
        verificarAcceso();
        real.eliminarSala(sala);
    }

    @Override
    public void cambiarTipoAsiento(Sala sala, String codigoAsiento, TipoAsiento tipo) {
        verificarAcceso();
        real.cambiarTipoAsiento(sala, codigoAsiento, tipo);
    }

    @Override
    public Funcion crearFuncion(Pelicula pelicula, Sala sala, LocalDateTime inicio, double precioBase) {
        verificarAcceso();
        return real.crearFuncion(pelicula, sala, inicio, precioBase);
    }

    @Override
    public void eliminarFuncion(Funcion funcion) {
        verificarAcceso();
        real.eliminarFuncion(funcion);
    }

    @Override
    public Producto crearProducto(String nombre, String categoria, double precio) {
        verificarAcceso();
        return real.crearProducto(nombre, categoria, precio);
    }

    @Override
    public Combo crearCombo(String nombre, double descuentoPorcentaje, List<ItemConfiteria> items) {
        verificarAcceso();
        return real.crearCombo(nombre, descuentoPorcentaje, items);
    }

    @Override
    public void eliminarItemConfiteria(ItemConfiteria item) {
        verificarAcceso();
        real.eliminarItemConfiteria(item);
    }

    @Override
    public Promocion crearPromocion(String nombre, TipoPromocion tipo, double valor, AlcancePromocion alcance) {
        verificarAcceso();
        return real.crearPromocion(nombre, tipo, valor, alcance);
    }

    @Override
    public void cambiarEstadoPromocion(Promocion promocion, boolean activa) {
        verificarAcceso();
        real.cambiarEstadoPromocion(promocion, activa);
    }

    @Override
    public void eliminarPromocion(Promocion promocion) {
        verificarAcceso();
        real.eliminarPromocion(promocion);
    }

    @Override
    public List<Cliente> listarClientes() {
        verificarAcceso();
        return real.listarClientes();
    }

    @Override
    public List<Compra> listarCompras() {
        verificarAcceso();
        return real.listarCompras();
    }

    @Override
    public List<SolicitudRecarga> solicitudesPendientes() {
        verificarAcceso();
        return real.solicitudesPendientes();
    }

    @Override
    public void aprobarSolicitud(SolicitudRecarga solicitud) {
        verificarAcceso();
        real.aprobarSolicitud(solicitud);
    }

    @Override
    public void rechazarSolicitud(SolicitudRecarga solicitud) {
        verificarAcceso();
        real.rechazarSolicitud(solicitud);
    }

    @Override
    public void recargarTarjeta(Cliente cliente, double valor) {
        verificarAcceso();
        real.recargarTarjeta(cliente, valor);
    }

    @Override
    public void cambiarEstadoTarjeta(Cliente cliente, boolean activa) {
        verificarAcceso();
        real.cambiarEstadoTarjeta(cliente, activa);
    }

    @Override
    public List<Reembolso> cancelarFuncion(Funcion funcion) {
        verificarAcceso();
        return real.cancelarFuncion(funcion);
    }
}
