package co.edu.uniquindio.cinemauq.servicio.reembolso;

import co.edu.uniquindio.cinemauq.modelo.Compra;

/**
 * Política de reembolso.
 * Patrón Strategy – rol: Estrategia. Cada política calcula el valor a devolver.
 * SOLID – OCP: una política nueva es una clase nueva, sin cambiar las demás.
 */
public interface PoliticaReembolso {

    int getPorcentaje();

    String getDescripcion();

    double calcularReembolso(Compra compra);
}
