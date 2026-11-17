package co.edu.uniquindio.cinemauq.modelo;

/**
 * Ítem de confitería: un producto o un combo.
 * Patrón Composite – rol: Componente. El precio se pide igual a uno o a muchos.
 * SOLID – LSP: Producto y Combo se usan como ItemConfiteria sin distinguirlos.
 */
public abstract class ItemConfiteria implements Vendible {

    private final int id;
    private final String nombre;

    protected ItemConfiteria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /** "Producto" o "Combo". */
    public abstract String getTipo();

    public abstract String getDescripcion();

    /** Por defecto un ítem no contiene a otro (lo sobrescribe Combo). */
    public boolean contiene(ItemConfiteria item) {
        return false;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
