package co.edu.uniquindio.cinemauq.modelo;

/**
 * Entrada General: precio base de la función (RN-18).
 * Patrón Factory Method – rol: Producto concreto.
 */
public class EntradaGeneral extends Entrada {

    private static final double FACTOR = 1.0;

    public EntradaGeneral(Funcion funcion, Asiento asiento) {
        super(funcion, asiento);
    }

    @Override
    public TipoAsiento getTipo() {
        return TipoAsiento.GENERAL;
    }

    @Override
    public double getPrecio() {
        return Math.round(getFuncion().getPrecioBase() * FACTOR);
    }
}
