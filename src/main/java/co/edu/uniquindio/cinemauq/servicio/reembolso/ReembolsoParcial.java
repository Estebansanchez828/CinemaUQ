package co.edu.uniquindio.cinemauq.servicio.reembolso;

import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón Strategy – rol: Estrategia concreta. Cancelación entre 2 y 24 horas: devuelve el 80 % (RN-07). */
public class ReembolsoParcial implements PoliticaReembolso {

    @Override
    public int getPorcentaje() {
        return 80;
    }

    @Override
    public String getDescripcion() {
        return "Cancelación entre 2 y 24 horas";
    }

    @Override
    public double calcularReembolso(Compra compra) {
        return Math.round(compra.getValorPagado() * getPorcentaje() / 100.0);
    }
}
