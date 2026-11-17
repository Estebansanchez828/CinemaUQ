package co.edu.uniquindio.cinemauq.modelo;

/**
 * Entrada Accesible: precio base - 20 % (RN-18).
 * Patrón Factory Method – rol: Producto concreto.
 */
public class EntradaAccesible extends Entrada {

    private static final double FACTOR = 0.8;

    public EntradaAccesible(Funcion funcion, Asiento asiento) {
        super(funcion, asiento);
    }

    @Override
    public TipoAsiento getTipo() {
        return TipoAsiento.ACCESIBLE;
    }

    @Override
    public double getPrecio() {
        return Math.round(getFuncion().getPrecioBase() * FACTOR);
    }
}
