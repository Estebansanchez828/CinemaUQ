package co.edu.uniquindio.cinemauq.servicio.reembolso;

import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón Strategy – rol: Estrategia concreta. Función cancelada por el cine: devuelve el 100 % (RN-09). */
public class ReembolsoFuncionCancelada implements PoliticaReembolso {

    @Override
    public int getPorcentaje() {
        return 100;
    }

    @Override
    public String getDescripcion() {
        return "Función cancelada por el cine";
    }

    @Override
    public double calcularReembolso(Compra compra) {
        return Math.round(compra.getValorPagado() * getPorcentaje() / 100.0);
    }
}
