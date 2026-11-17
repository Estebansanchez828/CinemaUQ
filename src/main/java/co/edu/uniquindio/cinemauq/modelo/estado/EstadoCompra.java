package co.edu.uniquindio.cinemauq.modelo.estado;

import co.edu.uniquindio.cinemauq.modelo.Compra;

/**
 * Estado de una compra.
 * Patrón State – rol: Estado. Cada estado decide qué operaciones permite (RN-26).
 */
public interface EstadoCompra {

    String getNombre();

    void pagar(Compra compra);

    void cancelar(Compra compra);

    void reembolsar(Compra compra);
}
