package co.edu.uniquindio.cinemauq.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente del cine. Composición 1 a 1 con su tarjeta virtual y su cuenta de puntos
 * (se crean con el cliente y no existen sin él).
 */
public class Cliente extends Usuario {

    private final TarjetaVirtual tarjeta;
    private final CuentaPuntos cuentaPuntos = new CuentaPuntos();
    private final List<Notificacion> notificaciones = new ArrayList<>();

    public Cliente(String nombre, String usuario, String clave, String numeroTarjeta) {
        super(nombre, usuario, clave);
        this.tarjeta = new TarjetaVirtual(numeroTarjeta);
    }

    @Override
    public boolean esAdministrador() {
        return false;
    }

    @Override
    public String getRol() {
        return "Cliente";
    }

    public void agregarNotificacion(String mensaje) {
        notificaciones.add(0, new Notificacion(mensaje, LocalDateTime.now()));
    }

    public TarjetaVirtual getTarjeta() {
        return tarjeta;
    }

    public CuentaPuntos getCuentaPuntos() {
        return cuentaPuntos;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
}
