package co.edu.uniquindio.cinemauq.servicio.fabrica;

import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.EntradaAccesible;
import co.edu.uniquindio.cinemauq.modelo.Funcion;

/** Patrón Factory Method – rol: Creador concreto (crea EntradaAccesible). */
public class CreadorEntradaAccesible extends CreadorEntrada {

    @Override
    protected Entrada crearEntrada(Funcion funcion, Asiento asiento) {
        return new EntradaAccesible(funcion, asiento);
    }
}
