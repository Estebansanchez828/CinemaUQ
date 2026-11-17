package co.edu.uniquindio.cinemauq.servicio.evento;

import co.edu.uniquindio.cinemauq.modelo.Compra;

import java.time.LocalDateTime;

/** Información que reciben los observadores: qué pasó, con qué compra y por qué valor. */
public class EventoCompra {

    private final TipoEvento tipo;
    private final Compra compra;
    private final double valor;
    private final LocalDateTime fecha = LocalDateTime.now();

    public EventoCompra(TipoEvento tipo, Compra compra, double valor) {
        this.tipo = tipo;
        this.compra = compra;
        this.valor = valor;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public Compra getCompra() {
        return compra;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
