package co.edu.uniquindio.cinemauq.servicio.fabrica;

import co.edu.uniquindio.cinemauq.excepciones.AsientoNoDisponibleException;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;

import java.util.EnumMap;
import java.util.Map;

/**
 * Patrón Factory Method – rol: Creador.
 * emitir() es la operación común; crearEntrada() es el método fábrica que
 * cada creador concreto implementa para devolver su tipo de entrada.
 * SOLID – OCP: un tipo de asiento nuevo = un creador nuevo, sin tocar este código.
 */
public abstract class CreadorEntrada {

    private static final Map<TipoAsiento, CreadorEntrada> CREADORES = new EnumMap<>(TipoAsiento.class);

    static {
        CREADORES.put(TipoAsiento.GENERAL, new CreadorEntradaGeneral());
        CREADORES.put(TipoAsiento.PREFERENCIAL, new CreadorEntradaPreferencial());
        CREADORES.put(TipoAsiento.ACCESIBLE, new CreadorEntradaAccesible());
    }

    /** Método fábrica. */
    protected abstract Entrada crearEntrada(Funcion funcion, Asiento asiento);

    /** Operación del creador: valida el asiento (RN-02) y usa el método fábrica. */
    public Entrada emitir(Funcion funcion, Asiento asiento) {
        if (funcion.estaOcupado(asiento)) {
            throw new AsientoNoDisponibleException("El asiento " + asiento.getCodigo()
                    + " ya está vendido para esta función.");
        }
        return crearEntrada(funcion, asiento);
    }

    /** Devuelve el creador que corresponde al tipo del asiento. */
    public static CreadorEntrada segunTipo(TipoAsiento tipo) {
        return CREADORES.get(tipo);
    }
}
