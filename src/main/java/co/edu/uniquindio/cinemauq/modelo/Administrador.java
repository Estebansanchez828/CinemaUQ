package co.edu.uniquindio.cinemauq.modelo;

/** Administrador del cine. Es el único que pasa el Proxy de administración. */
public class Administrador extends Usuario {

    public Administrador(String nombre, String usuario, String clave) {
        super(nombre, usuario, clave);
    }

    @Override
    public boolean esAdministrador() {
        return true;
    }

    @Override
    public String getRol() {
        return "Administrador";
    }
}
