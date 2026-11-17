package co.edu.uniquindio.cinemauq.servicio.reembolso;

import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón Strategy – rol: Estrategia concreta. Cancelación con más de 24 horas: devuelve el 100 % (RN-06). */
public class ReembolsoCompleto implements PoliticaReembolso {

    @Override
    public int getPorcentaje() {
        return 100;
    }

    @Override
    public String getDescripcion() {
        return "Cancelación con más de 24 horas";
    }

    @Override
    public double calcularReembolso(Compra compra) {
        return Math.round(compra.getValorPagado() * getPorcentaje() / 100.0);
    }
}
