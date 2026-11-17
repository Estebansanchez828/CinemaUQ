package co.edu.uniquindio.cinemauq.servicio.admin;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionCruzadaException;
import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Combo;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.EstadoFuncion;
import co.edu.uniquindio.cinemauq.modelo.EstadoSolicitud;
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
import co.edu.uniquindio.cinemauq.servicio.ServicioReembolso;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica real de administración (no revisa permisos: eso lo hace el Proxy).
 * Patrón Proxy – rol: Sujeto real.
 * SOLID – DIP: recibe ServicioReembolso (interfaz) por el constructor.
 */
public class ServicioAdmin implements AdministracionCatalogo, AdministracionOperaciones {

    private final RepositorioCinema repositorio;
    private final ServicioReembolso servicioReembolso;

    public ServicioAdmin(RepositorioCinema repositorio, ServicioReembolso servicioReembolso) {
        this.repositorio = repositorio;
        this.servicioReembolso = servicioReembolso;
    }

    // ===================== Catálogo =====================

    @Override
    public Pelicula crearPelicula(String titulo, String genero, int duracionMinutos, String clasificacion) {
        obligatorio(titulo, "título");
        if (duracionMinutos < 30 || duracionMinutos > 300) {
            throw new DatoInvalidoException("La duración debe estar entre 30 y 300 minutos.");
        }
        Pelicula pelicula = new Pelicula(repositorio.siguienteId(), titulo.trim(), texto(genero),
                duracionMinutos, texto(clasificacion));
        repositorio.getPeliculas().add(pelicula);
        return pelicula;
    }

    /** RN-25: no se elimina una película con funciones programadas. */
    @Override
    public void eliminarPelicula(Pelicula pelicula) {
        boolean tieneFunciones = repositorio.getFunciones().stream()
                .anyMatch(f -> f.getPelicula() == pelicula && esProgramadaFutura(f));
        if (tieneFunciones) {
            throw new OperacionNoPermitidaException("La película tiene funciones programadas.");
        }
        repositorio.getPeliculas().remove(pelicula);
    }

    @Override
    public Sala crearSala(String nombre, int filas, int columnas) {
        obligatorio(nombre, "nombre de la sala");
        Sala sala = new Sala(repositorio.siguienteId(), nombre.trim(), filas, columnas);
        repositorio.getSalas().add(sala);
        return sala;
    }

    /** RN-25: no se elimina una sala con funciones programadas. */
    @Override
    public void eliminarSala(Sala sala) {
        boolean tieneFunciones = repositorio.getFunciones().stream()
                .anyMatch(f -> f.getSala() == sala && esProgramadaFutura(f));
        if (tieneFunciones) {
            throw new OperacionNoPermitidaException("La sala tiene funciones programadas.");
        }
        repositorio.getSalas().remove(sala);
    }

    @Override
    public void cambiarTipoAsiento(Sala sala, String codigoAsiento, TipoAsiento tipo) {
        sala.buscarAsiento(codigoAsiento).setTipo(tipo);
    }

    /** RN-01 y RN-14: función completa, en el futuro y sin cruces en la misma sala. */
    @Override
    public Funcion crearFuncion(Pelicula pelicula, Sala sala, LocalDateTime inicio, double precioBase) {
        if (pelicula == null || sala == null || inicio == null) {
            throw new DatoInvalidoException("La función debe tener película, sala, fecha y hora.");
        }
        if (!inicio.isAfter(LocalDateTime.now())) {
            throw new DatoInvalidoException("La función debe programarse en el futuro.");
        }
        if (precioBase <= 0) {
            throw new DatoInvalidoException("El precio base debe ser mayor que 0.");
        }
        Funcion nueva = new Funcion(repositorio.siguienteId(), pelicula, sala, inicio, precioBase);
        for (Funcion existente : repositorio.getFunciones()) {
            if (nueva.seCruzaCon(existente)) {
                throw new FuncionCruzadaException("Se cruza con la función " + existente
                        + " (termina a las " + Formato.fechaHora(existente.getFin()) + ").");
            }
        }
        repositorio.getFunciones().add(nueva);
        return nueva;
    }

    /** RN-25: una función con entradas vendidas no se elimina, se cancela. */
    @Override
    public void eliminarFuncion(Funcion funcion) {
        if (!funcion.getAsientosOcupados().isEmpty() || !repositorio.comprasDeFuncion(funcion).isEmpty()) {
            throw new OperacionNoPermitidaException("La función tiene entradas vendidas: debe cancelarla.");
        }
        repositorio.getFunciones().remove(funcion);
    }

    @Override
    public Producto crearProducto(String nombre, String categoria, double precio) {
        obligatorio(nombre, "nombre del producto");
        if (precio <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor que 0.");
        }
        Producto producto = new Producto(repositorio.siguienteId(), nombre.trim(), texto(categoria), precio);
        repositorio.getConfiteria().add(producto);
        return producto;
    }

    @Override
    public Combo crearCombo(String nombre, double descuentoPorcentaje, List<ItemConfiteria> items) {
        obligatorio(nombre, "nombre del combo");
        if (items == null || items.isEmpty()) {
            throw new DatoInvalidoException("El combo debe tener al menos un producto o combo.");
        }
        Combo combo = new Combo(repositorio.siguienteId(), nombre.trim(), descuentoPorcentaje);
        items.forEach(combo::agregar);
        repositorio.getConfiteria().add(combo);
        return combo;
    }

    /** RN-25: no se elimina un ítem que forma parte de un combo. */
    @Override
    public void eliminarItemConfiteria(ItemConfiteria item) {
        boolean usado = repositorio.getCombos().stream().anyMatch(c -> c != item && c.contiene(item));
        if (usado) {
            throw new OperacionNoPermitidaException("'" + item.getNombre() + "' hace parte de un combo.");
        }
        repositorio.getConfiteria().remove(item);
    }

    @Override
    public Promocion crearPromocion(String nombre, TipoPromocion tipo, double valor, AlcancePromocion alcance) {
        obligatorio(nombre, "nombre de la promoción");
        if (tipo == null || alcance == null || valor <= 0) {
            throw new DatoInvalidoException("La promoción debe tener tipo, alcance y un valor mayor que 0.");
        }
        if (tipo == TipoPromocion.PORCENTAJE && valor >= 100) {
            throw new DatoInvalidoException("El porcentaje debe ser menor que 100.");
        }
        Promocion promocion = new Promocion(repositorio.siguienteId(), nombre.trim(), tipo, valor, alcance);
        repositorio.getPromociones().add(promocion);
        return promocion;
    }

    @Override
    public void cambiarEstadoPromocion(Promocion promocion, boolean activa) {
        promocion.setActiva(activa);
    }

    @Override
    public void eliminarPromocion(Promocion promocion) {
        repositorio.getPromociones().remove(promocion);
    }

    // ===================== Operaciones =====================

    @Override
    public List<Cliente> listarClientes() {
        return repositorio.getClientes();
    }

    @Override
    public List<Compra> listarCompras() {
        return repositorio.getCompras();
    }

    @Override
    public List<SolicitudRecarga> solicitudesPendientes() {
        return repositorio.getSolicitudes().stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE).toList();
    }

    /** RN-05: la recarga queda registrada en los movimientos de la tarjeta. */
    @Override
    public void aprobarSolicitud(SolicitudRecarga solicitud) {
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new OperacionNoPermitidaException("La solicitud ya fue atendida.");
        }
        solicitud.getCliente().getTarjeta().recargar(solicitud.getValor(),
                "Recarga aprobada (solicitud " + solicitud.getId() + ")", LocalDateTime.now());
        solicitud.aprobar();
        solicitud.getCliente().agregarNotificacion("Su recarga de " + Formato.dinero(solicitud.getValor())
                + " fue aprobada.");
    }

    @Override
    public void rechazarSolicitud(SolicitudRecarga solicitud) {
        solicitud.rechazar();
        solicitud.getCliente().agregarNotificacion("Su solicitud de recarga de "
                + Formato.dinero(solicitud.getValor()) + " fue rechazada.");
    }

    @Override
    public void recargarTarjeta(Cliente cliente, double valor) {
        cliente.getTarjeta().recargar(valor, "Recarga realizada por el administrador", LocalDateTime.now());
        cliente.agregarNotificacion("El administrador recargó su tarjeta con " + Formato.dinero(valor) + ".");
    }

    @Override
    public void cambiarEstadoTarjeta(Cliente cliente, boolean activa) {
        cliente.getTarjeta().setActiva(activa);
        cliente.agregarNotificacion(activa ? "Su tarjeta fue activada." : "Su tarjeta fue bloqueada.");
    }

    /** RN-09: si el cine cancela la función, se devuelve el 100 % a todos. */
    @Override
    public List<Reembolso> cancelarFuncion(Funcion funcion) {
        return servicioReembolso.reembolsarFuncionCancelada(funcion);
    }

    // ===================== Ayudas =====================

    private boolean esProgramadaFutura(Funcion funcion) {
        return funcion.getEstado() == EstadoFuncion.PROGRAMADA && funcion.getInicio().isAfter(LocalDateTime.now());
    }

    private void obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " es obligatorio.");
        }
    }

    private String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
