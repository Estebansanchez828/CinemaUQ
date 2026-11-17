package co.edu.uniquindio.cinemauq.modelo;

/**
 * Entrada para una función y un asiento.
 * Patrón Factory Method – rol: Producto.
 * Patrón Decorator – rol: Componente concreto (se le pueden aplicar promociones).
 * SOLID – LSP: las tres subclases se usan como Entrada sin cambiar el resultado esperado.
 */
public abstract class Entrada implements Vendible {

    private final Funcion funcion;
    private final Asiento asiento;

    protected Entrada(Funcion funcion, Asiento asiento) {
        this.funcion = funcion;
        this.asiento = asiento;
    }

    public abstract TipoAsiento getTipo();

    @Override
    public String getNombre() {
        return "Entrada " + getTipo() + " " + asiento.getCodigo() + " - " + funcion.getPelicula().getTitulo();
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }
}
