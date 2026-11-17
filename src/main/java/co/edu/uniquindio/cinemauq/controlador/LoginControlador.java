package co.edu.uniquindio.cinemauq.controlador;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.servicio.ServicioAutenticacion;
import co.edu.uniquindio.cinemauq.servicio.Sistema;

/** Controlador (MVC) del inicio de sesión y el registro. */
public class LoginControlador {

    private final ServicioAutenticacion servicioAutenticacion;

    public LoginControlador(Sistema sistema) {
        this.servicioAutenticacion = sistema.getServicioAutenticacion();
    }

    public Usuario iniciarSesion(String usuario, String clave) {
        return servicioAutenticacion.iniciarSesion(usuario, clave);
    }

    public Cliente registrar(String nombre, String usuario, String clave, String confirmacion) {
        if (clave == null || !clave.equals(confirmacion)) {
            throw new co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException("Las contraseñas no coinciden.");
        }
        return servicioAutenticacion.registrar(nombre, usuario, clave);
    }
}
