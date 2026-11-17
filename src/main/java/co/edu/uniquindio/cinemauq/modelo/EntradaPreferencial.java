package co.edu.uniquindio.cinemauq.modelo;

/**
 * Entrada Preferencial: precio base + 25 % (RN-18).
 * Patrón Factory Method – rol: Producto concreto.
 */
public class EntradaPreferencial extends Entrada {

    private static final double FACTOR = 1.25;

    public EntradaPreferencial(Funcion funcion, Asiento asiento) {
        super(funcion, asiento);
    }

    @Override
    public TipoAsiento getTipo() {
        return TipoAsiento.PREFERENCIAL;
    }

    @Override
    public double getPrecio() {
        return Math.round(getFuncion().getPrecioBase() * FACTOR);
    }
}
