package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Vendible;

import java.util.List;

/** Programa de puntos CinemaUQ (RN-11, RN-12). SOLID – ISP. */
public interface ServicioPuntos {

    int calcularPuntosNecesarios(List<? extends Vendible> vendibles);

    int consultarSaldo(Cliente cliente);

    void verificarPuntos(Cliente cliente, int puntos);

    void acumular(Compra compra);

    void redimir(Compra compra);

    void revertir(Compra compra);
}
