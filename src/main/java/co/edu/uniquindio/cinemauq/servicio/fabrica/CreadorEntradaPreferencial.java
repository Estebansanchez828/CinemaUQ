package co.edu.uniquindio.cinemauq.servicio.fabrica;

import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.EntradaPreferencial;
import co.edu.uniquindio.cinemauq.modelo.Funcion;

/** Patrón Factory Method – rol: Creador concreto (crea EntradaPreferencial). */
public class CreadorEntradaPreferencial extends CreadorEntrada {

    @Override
    protected Entrada crearEntrada(Funcion funcion, Asiento asiento) {
        return new EntradaPreferencial(funcion, asiento);
    }
}
