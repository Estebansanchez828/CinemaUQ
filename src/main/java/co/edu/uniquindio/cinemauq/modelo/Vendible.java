package co.edu.uniquindio.cinemauq.modelo;

/**
 * Todo lo que se puede vender: entradas, productos y combos.
 * Patrón Decorator – rol: Componente (las promociones decoran un Vendible).
 * SOLID – ISP: interfaz mínima, solo nombre y precio.
 */
public interface Vendible {

    String getNombre();

    double getPrecio();
}
