package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.excepciones.AsientoNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoDisponibleException;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Implementación de la gestión de asientos. */
public class ServicioAsientosImpl implements ServicioAsientos {

    @Override
    public void verificarDisponibles(Funcion funcion, List<Asiento> asientos) {
        if (!funcion.estaDisponibleParaVenta(LocalDateTime.now())) {
            throw new FuncionNoDisponibleException("La función " + funcion + " está cancelada o ya empezó.");
        }
        Set<String> vistos = new HashSet<>();
        for (Asiento asiento : asientos) {
            if (!vistos.add(asiento.getCodigo())) {
                throw new DatoInvalidoException("El asiento " + asiento + " está repetido.");
            }
            if (funcion.estaOcupado(asiento)) {
                throw new AsientoNoDisponibleException("El asiento " + asiento + " ya está vendido para esta función.");
            }
        }
    }

    @Override
    public void ocupar(Funcion funcion, List<Asiento> asientos) {
        for (Asiento asiento : asientos) {
            funcion.ocupar(asiento);
        }
    }

    @Override
    public void liberar(Compra compra) {
        for (Entrada entrada : compra.getEntradas()) {
            compra.getFuncion().liberar(entrada.getAsiento());
        }
    }
}
