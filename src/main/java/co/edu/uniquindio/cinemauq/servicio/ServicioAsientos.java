package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;

import java.util.List;

/** Disponibilidad y ocupación de asientos (RN-02, RN-15). SOLID – ISP. */
public interface ServicioAsientos {

    void verificarDisponibles(Funcion funcion, List<Asiento> asientos);

    void ocupar(Funcion funcion, List<Asiento> asientos);

    void liberar(Compra compra);
}
