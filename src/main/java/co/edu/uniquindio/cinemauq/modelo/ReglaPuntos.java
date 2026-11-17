package co.edu.uniquindio.cinemauq.modelo;

/**
 * Regla del programa de puntos CinemaUQ (un solo lugar para estudiarla y cambiarla).
 * RP-1: por cada $100 pagados con la tarjeta se gana 1 punto.
 * RP-6: redimir cuesta el precio base dividido entre 10 (1 punto vale $10).
 */
public final class ReglaPuntos {

    public static final int PESOS_POR_PUNTO_GANADO = 100;
    public static final int PESOS_POR_PUNTO_REDIMIDO = 10;

    private ReglaPuntos() {
    }

    public static int puntosGanados(double valorPagado) {
        return (int) (valorPagado / PESOS_POR_PUNTO_GANADO);
    }

    public static int puntosNecesarios(double precioBase) {
        return (int) Math.ceil(precioBase / PESOS_POR_PUNTO_REDIMIDO);
    }
}
