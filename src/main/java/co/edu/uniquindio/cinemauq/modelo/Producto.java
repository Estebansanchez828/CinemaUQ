package co.edu.uniquindio.cinemauq.modelo;

/**
 * Producto individual de confitería.
 * Patrón Composite – rol: Hoja.
 */
public class Producto extends ItemConfiteria {

    private final String categoria;
    private final double precio;

    public Producto(int id, String nombre, String categoria, double precio) {
        super(id, nombre);
        this.categoria = categoria;
        this.precio = precio;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    @Override
    public String getTipo() {
        return "Producto";
    }

    @Override
    public String getDescripcion() {
        return categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}
