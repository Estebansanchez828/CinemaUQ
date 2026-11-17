package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

import java.util.List;

/** Aplica las promociones activas a entradas y confitería. SOLID – ISP. */
public interface ServicioPromociones {

    Vendible aplicarPromociones(Vendible vendible);

    double calcularDescuento(List<? extends Vendible> vendibles);

    List<Promocion> promocionesActivas();
}
