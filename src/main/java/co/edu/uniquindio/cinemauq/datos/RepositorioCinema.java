package co.edu.uniquindio.cinemauq.datos;

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
import co.edu.uniquindio.cinemauq.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio central en memoria: la única fuente de datos de la aplicación.
 * Patrón Singleton – rol: Singleton (constructor privado + getInstancia()).
 */
public class RepositorioCinema {

    private static RepositorioCinema instancia;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();
    private final List<Funcion> funciones = new ArrayList<>();
    private final List<ItemConfiteria> confiteria = new ArrayList<>();
    private final List<Promocion> promociones = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<SolicitudRecarga> solicitudes = new ArrayList<>();
    private final List<Reembolso> reembolsos = new ArrayList<>();

    private int consecutivoId = 100;
    private int consecutivoCompra = 0;
    private int consecutivoTarjeta = 0;

    private RepositorioCinema() {
    }

    public static synchronized RepositorioCinema getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioCinema();
        }
        return instancia;
    }

    // ----- Consecutivos -----

    public int siguienteId() {
        return ++consecutivoId;
    }

    public String siguienteCodigoCompra() {
        return String.format("C-%04d", ++consecutivoCompra);
    }

    public String siguienteNumeroTarjeta() {
        return String.format("CINE-%04d", ++consecutivoTarjeta);
    }

    // ----- Consultas -----

    public boolean estaVacio() {
        return usuarios.isEmpty();
    }

    public Optional<Usuario> buscarUsuario(String usuario) {
        return usuarios.stream().filter(u -> u.getUsuario().equalsIgnoreCase(usuario)).findFirst();
    }

    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Cliente cliente) {
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public List<Compra> comprasDe(Cliente cliente) {
        return compras.stream().filter(c -> c.getCliente() == cliente).toList();
    }

    public List<Compra> comprasDeFuncion(Funcion funcion) {
        return compras.stream().filter(c -> c.getFuncion() == funcion).toList();
    }

    public List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<>();
        for (ItemConfiteria item : confiteria) {
            if (item instanceof Producto p) {
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Combo> getCombos() {
        List<Combo> lista = new ArrayList<>();
        for (ItemConfiteria item : confiteria) {
            if (item instanceof Combo c) {
                lista.add(c);
            }
        }
        return lista;
    }

    // ----- Listas -----

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public List<ItemConfiteria> getConfiteria() {
        return confiteria;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public List<SolicitudRecarga> getSolicitudes() {
        return solicitudes;
    }

    public List<Reembolso> getReembolsos() {
        return reembolsos;
    }
}
