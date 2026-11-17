package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;
import co.edu.uniquindio.cinemauq.modelo.Vendible;
import co.edu.uniquindio.cinemauq.servicio.promocion.DescuentoPorcentaje;
import co.edu.uniquindio.cinemauq.servicio.promocion.DescuentoValorFijo;

import java.util.Comparator;
import java.util.List;

/**
 * Envuelve cada ítem con los decoradores de las promociones activas que le aplican.
 * Patrón Decorator – rol: Cliente (arma la cadena de decoradores).
 * RN-20: primero las de porcentaje, luego las de valor fijo; el precio nunca baja de 0.
 */
public class ServicioPromocionesImpl implements ServicioPromociones {

    private final RepositorioCinema repositorio;

    public ServicioPromocionesImpl(RepositorioCinema repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Vendible aplicarPromociones(Vendible vendible) {
        Vendible resultado = vendible;
        for (Promocion promocion : promocionesActivas()) {
            if (promocion.aplicaA(vendible)) {
                resultado = promocion.getTipo() == TipoPromocion.PORCENTAJE
                        ? new DescuentoPorcentaje(resultado, promocion)
                        : new DescuentoValorFijo(resultado, promocion);
            }
        }
        return resultado;
    }

    @Override
    public double calcularDescuento(List<? extends Vendible> vendibles) {
        double descuento = 0;
        for (Vendible vendible : vendibles) {
            descuento += vendible.getPrecio() - aplicarPromociones(vendible).getPrecio();
        }
        return descuento;
    }

    @Override
    public List<Promocion> promocionesActivas() {
        return repositorio.getPromociones().stream()
                .filter(Promocion::isActiva)
                .sorted(Comparator.comparing(p -> p.getTipo() == TipoPromocion.PORCENTAJE ? 0 : 1))
                .toList();
    }
}
