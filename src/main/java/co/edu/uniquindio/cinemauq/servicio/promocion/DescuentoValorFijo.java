package co.edu.uniquindio.cinemauq.servicio.promocion;

import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

/** Patrón Decorator – rol: Decorador concreto (descuento de un valor fijo). */
public class DescuentoValorFijo extends PromocionDecorador {

    public DescuentoValorFijo(Vendible componente, Promocion promocion) {
        super(componente, promocion);
    }

    @Override
    public double getPrecio() {
        return Math.max(0, componente.getPrecio() - promocion.getValor());
    }
}
