package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDateTime;

/** Mensaje que recibe el cliente (lo crea el ObservadorNotificaciones). */
public class Notificacion {

    private final String mensaje;
    private final LocalDateTime fecha;

    public Notificacion(String mensaje, LocalDateTime fecha) {
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return Formato.fechaHora(fecha) + " - " + mensaje;
    }
}
