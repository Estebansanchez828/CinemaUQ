package co.edu.uniquindio.cinemauq.servicio.promocion;

import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

/**
 * Patrón Decorator – rol: Decorador. Envuelve un Vendible y le cambia el precio
 * sin modificar las clases Entrada, Producto o Combo.
 * SOLID – OCP: una promoción nueva es un decorador nuevo.
 */
public abstract class PromocionDecorador implements Vendible {

    protected final Vendible componente;
    protected final Promocion promocion;

    protected PromocionDecorador(Vendible componente, Promocion promocion) {
        this.componente = componente;
        this.promocion = promocion;
    }

    @Override
    public String getNombre() {
        return componente.getNombre() + " [" + promocion.getNombre() + "]";
    }
}
