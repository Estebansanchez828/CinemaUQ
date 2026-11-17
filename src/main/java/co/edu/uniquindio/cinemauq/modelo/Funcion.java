package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.AsientoNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Función: película + sala + fecha y hora (RN-01). Guarda los asientos ocupados (RN-02).
 */
public class Funcion {

    /** Minutos de aseo entre funciones de la misma sala (RN-14). */
    public static final int MINUTOS_ASEO = 15;

    private final int id;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDateTime inicio;
    private final double precioBase;
    private EstadoFuncion estado = EstadoFuncion.PROGRAMADA;
    private final Set<String> asientosOcupados = new LinkedHashSet<>();

    public Funcion(int id, Pelicula pelicula, Sala sala, LocalDateTime inicio, double precioBase) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.inicio = inicio;
        this.precioBase = precioBase;
    }

    public LocalDateTime getFin() {
        return inicio.plusMinutes(pelicula.getDuracionMinutos() + MINUTOS_ASEO);
    }

    /** RN-14: dos funciones programadas de la misma sala no se pueden cruzar. */
    public boolean seCruzaCon(Funcion otra) {
        return otra != this
                && estado == EstadoFuncion.PROGRAMADA
                && otra.estado == EstadoFuncion.PROGRAMADA
                && sala == otra.sala
                && inicio.isBefore(otra.getFin())
                && otra.inicio.isBefore(getFin());
    }

    public boolean estaOcupado(Asiento asiento) {
        return asientosOcupados.contains(asiento.getCodigo());
    }

    /** RN-02: un asiento no se vende dos veces para la misma función. */
    public void ocupar(Asiento asiento) {
        if (!asientosOcupados.add(asiento.getCodigo())) {
            throw new AsientoNoDisponibleException("El asiento " + asiento.getCodigo()
                    + " ya está vendido para esta función.");
        }
    }

    public void liberar(Asiento asiento) {
        asientosOcupados.remove(asiento.getCodigo());
    }

    /** RN-15: solo se vende si está programada y aún no empieza. */
    public boolean estaDisponibleParaVenta(LocalDateTime ahora) {
        return estado == EstadoFuncion.PROGRAMADA && ahora.isBefore(inicio);
    }

    public double horasRestantes(LocalDateTime ahora) {
        return Duration.between(ahora, inicio).toMinutes() / 60.0;
    }

    public double getOcupacion() {
        return asientosOcupados.size() * 100.0 / sala.getCapacidad();
    }

    public void cancelar() {
        if (estado == EstadoFuncion.CANCELADA) {
            throw new OperacionNoPermitidaException("La función ya está cancelada.");
        }
        estado = EstadoFuncion.CANCELADA;
    }

    /** Programada, Realizada (ya pasó) o Cancelada; para tablas y reportes. */
    public String getEstadoTexto() {
        if (estado == EstadoFuncion.CANCELADA) {
            return "Cancelada";
        }
        return inicio.isAfter(LocalDateTime.now()) ? "Programada" : "Realizada";
    }

    public int getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public Set<String> getAsientosOcupados() {
        return asientosOcupados;
    }

    @Override
    public String toString() {
        return pelicula.getTitulo() + " | " + sala.getNombre() + " | " + Formato.fechaHora(inicio);
    }
}
