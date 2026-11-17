package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Sala del cine. Composición 1 a 1..* con sus asientos: los crea en una cuadrícula.
 * Fila A: asientos 1 y 2 accesibles. Dos últimas filas: preferenciales. Resto: generales.
 */
public class Sala {

    private final int id;
    private final String nombre;
    private final int filas;
    private final int columnas;
    private final List<Asiento> asientos = new ArrayList<>();

    public Sala(int id, String nombre, int filas, int columnas) {
        if (filas < 1 || filas > 15 || columnas < 1 || columnas > 20) {
            throw new DatoInvalidoException("La sala debe tener entre 1 y 15 filas y entre 1 y 20 columnas.");
        }
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        for (int f = 0; f < filas; f++) {
            String letra = String.valueOf((char) ('A' + f));
            for (int c = 1; c <= columnas; c++) {
                TipoAsiento tipo = TipoAsiento.GENERAL;
                if (filas >= 4 && f >= filas - 2) {
                    tipo = TipoAsiento.PREFERENCIAL;
                }
                if (f == 0 && c <= 2) {
                    tipo = TipoAsiento.ACCESIBLE;
                }
                asientos.add(new Asiento(letra, c, tipo));
            }
        }
    }

    public Asiento buscarAsiento(String codigo) {
        return asientos.stream()
                .filter(a -> a.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElseThrow(() -> new DatoInvalidoException("El asiento " + codigo + " no existe en " + nombre));
    }

    public int getCapacidad() {
        return asientos.size();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
