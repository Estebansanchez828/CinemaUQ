package co.edu.uniquindio.cinemauq.servicio.promocion;

import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

/** Patrón Decorator – rol: Decorador concreto (descuento en porcentaje). */
public class DescuentoPorcentaje extends PromocionDecorador {

    public DescuentoPorcentaje(Vendible componente, Promocion promocion) {
        super(componente, promocion);
    }

    @Override
    public double getPrecio() {
        return Math.max(0, Math.round(componente.getPrecio() * (1 - promocion.getValor() / 100)));
    }
}
