package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;

/** Clase abstracta común a los reportes: guarda el repositorio y se muestra con su nombre. */
public abstract class ReporteBase implements Reporte {

    protected final RepositorioCinema repositorio;

    protected ReporteBase(RepositorioCinema repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
