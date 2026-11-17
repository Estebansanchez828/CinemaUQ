package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Usuario;

/** Registro e inicio de sesión (RF-001, RF-002). SOLID – ISP: interfaz pequeña. */
public interface ServicioAutenticacion {

    Usuario iniciarSesion(String usuario, String clave);

    Cliente registrar(String nombre, String usuario, String clave);
}
