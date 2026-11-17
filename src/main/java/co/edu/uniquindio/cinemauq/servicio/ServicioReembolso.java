package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;

import java.util.List;

/**
 * Cancelaciones y reembolsos (RN-06 a RN-10). La implementación usa el patrón Strategy.
 * SOLID – DIP: la administración y la fachada dependen de esta interfaz, no de la clase concreta.
 */
public interface ServicioReembolso {

    Reembolso cancelarCompra(Compra compra);

    List<Reembolso> reembolsarFuncionCancelada(Funcion funcion);
}
