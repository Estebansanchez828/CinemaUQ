package co.edu.uniquindio.cinemauq.modelo;

/**
 * Usuario del sistema (clase abstracta).
 * SOLID – LSP: Cliente y Administrador pueden usarse donde se espera un Usuario
 * (por ejemplo, en el inicio de sesión) sin romper el comportamiento.
 */
public abstract class Usuario {

    private final String nombre;
    private final String usuario;
    private final String clave;

    protected Usuario(String nombre, String usuario, String clave) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
    }

    public boolean validarClave(String clave) {
        return this.clave.equals(clave);
    }

    /** Polimorfismo: cada subclase responde si puede administrar. */
    public abstract boolean esAdministrador();

    public abstract String getRol();

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return nombre + " (" + usuario + ")";
    }
}
