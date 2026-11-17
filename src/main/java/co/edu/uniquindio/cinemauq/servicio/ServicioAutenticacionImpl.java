package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.CredencialesInvalidasException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.UsuarioYaExisteException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Usuario;

/** Implementación del registro y el inicio de sesión. */
public class ServicioAutenticacionImpl implements ServicioAutenticacion {

    private final RepositorioCinema repositorio;

    public ServicioAutenticacionImpl(RepositorioCinema repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Usuario iniciarSesion(String usuario, String clave) {
        return repositorio.buscarUsuario(usuario == null ? "" : usuario.trim())
                .filter(u -> u.validarClave(clave))
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos."));
    }

    /** RN-13: usuario único; el cliente nace con tarjeta activa en $0 y cuenta de puntos vacía. */
    @Override
    public Cliente registrar(String nombre, String usuario, String clave) {
        if (nombre == null || nombre.isBlank() || usuario == null || usuario.isBlank()) {
            throw new DatoInvalidoException("El nombre y el usuario son obligatorios.");
        }
        if (clave == null || clave.length() < 4) {
            throw new DatoInvalidoException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (repositorio.buscarUsuario(usuario.trim()).isPresent()) {
            throw new UsuarioYaExisteException("El usuario '" + usuario.trim() + "' ya existe.");
        }
        Cliente cliente = new Cliente(nombre.trim(), usuario.trim(), clave, repositorio.siguienteNumeroTarjeta());
        cliente.agregarNotificacion("Bienvenido a CinemaUQ. Su tarjeta " + cliente.getTarjeta().getNumero()
                + " está activa con saldo $0. Solicite una recarga para comprar.");
        repositorio.getUsuarios().add(cliente);
        return cliente;
    }
}
