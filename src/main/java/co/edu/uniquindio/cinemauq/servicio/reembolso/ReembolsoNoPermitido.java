package co.edu.uniquindio.cinemauq.servicio.reembolso;

import co.edu.uniquindio.cinemauq.excepciones.CancelacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.Compra;

/** Patrón Strategy – rol: Estrategia concreta. Menos de 2 horas: no se permite cancelar (RN-08). */
public class ReembolsoNoPermitido implements PoliticaReembolso {

    @Override
    public int getPorcentaje() {
        return 0;
    }

    @Override
    public String getDescripcion() {
        return "Faltan menos de 2 horas";
    }

    @Override
    public double calcularReembolso(Compra compra) {
        throw new CancelacionNoPermitidaException("No se permite cancelar la compra " + compra.getCodigo()
                + ": faltan menos de 2 horas para la función o ya se realizó.");
    }
}
