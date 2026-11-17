package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.CancelacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.servicio.evento.EventoCompra;
import co.edu.uniquindio.cinemauq.servicio.evento.SujetoCompra;
import co.edu.uniquindio.cinemauq.servicio.evento.TipoEvento;
import co.edu.uniquindio.cinemauq.servicio.reembolso.PoliticaReembolso;
import co.edu.uniquindio.cinemauq.servicio.reembolso.ReembolsoCompleto;
import co.edu.uniquindio.cinemauq.servicio.reembolso.ReembolsoFuncionCancelada;
import co.edu.uniquindio.cinemauq.servicio.reembolso.ReembolsoNoPermitido;
import co.edu.uniquindio.cinemauq.servicio.reembolso.ReembolsoParcial;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cancelaciones y reembolsos.
 * Patrón Strategy – rol: Contexto (elige la política según las horas que faltan).
 * Usa el State de la compra para no reembolsar dos veces y el Observer para avisar.
 * SOLID – DIP: depende de interfaces (ServicioTarjeta, ServicioAsientos, SujetoCompra).
 */
public class ServicioReembolsoImpl implements ServicioReembolso {

    private final RepositorioCinema repositorio;
    private final ServicioTarjeta servicioTarjeta;
    private final ServicioAsientos servicioAsientos;
    private final SujetoCompra notificador;

    public ServicioReembolsoImpl(RepositorioCinema repositorio, ServicioTarjeta servicioTarjeta,
                                 ServicioAsientos servicioAsientos, SujetoCompra notificador) {
        this.repositorio = repositorio;
        this.servicioTarjeta = servicioTarjeta;
        this.servicioAsientos = servicioAsientos;
        this.notificador = notificador;
    }

    /** Elige la estrategia: más de 24 h = 100 %, entre 2 y 24 h = 80 %, menos de 2 h = no permitido. */
    public PoliticaReembolso elegirPolitica(Compra compra, LocalDateTime ahora) {
        double horas = compra.getFuncion().horasRestantes(ahora);
        if (horas > 24) {
            return new ReembolsoCompleto();
        }
        if (horas >= 2) {
            return new ReembolsoParcial();
        }
        return new ReembolsoNoPermitido();
    }

    @Override
    public Reembolso cancelarCompra(Compra compra) {
        if (compra.getFuncion() == null) {
            throw new CancelacionNoPermitidaException("Las compras solo de confitería no se cancelan (RN-22).");
        }
        if (compra.getMetodoPago() == MetodoPago.PUNTOS) {
            throw new CancelacionNoPermitidaException("Las compras pagadas con puntos no se cancelan (RN-24).");
        }
        LocalDateTime ahora = LocalDateTime.now();
        PoliticaReembolso politica = elegirPolitica(compra, ahora);
        double valor = politica.calcularReembolso(compra);   // Strategy (puede lanzar CancelacionNoPermitida)
        compra.cancelar();                                    // State (si ya fue reembolsada lanza ReembolsoDuplicado)
        servicioTarjeta.abonarReembolso(compra, valor, politica.getPorcentaje());
        return cerrarReembolso(compra, valor, politica, ahora, TipoEvento.COMPRA_REEMBOLSADA);
    }

    /** RN-09: el cine cancela la función y devuelve el 100 % a cada compra pagada. */
    @Override
    public List<Reembolso> reembolsarFuncionCancelada(Funcion funcion) {
        LocalDateTime ahora = LocalDateTime.now();
        if (!funcion.getInicio().isAfter(ahora)) {
            throw new OperacionNoPermitidaException("La función ya se realizó; no se puede cancelar.");
        }
        funcion.cancelar();
        PoliticaReembolso politica = new ReembolsoFuncionCancelada();
        List<Reembolso> reembolsos = new ArrayList<>();
        for (Compra compra : repositorio.comprasDeFuncion(funcion)) {
            if (!compra.estaPagada()) {
                continue;
            }
            double valor = politica.calcularReembolso(compra);
            compra.cancelar();
            if (valor > 0) {
                servicioTarjeta.abonarReembolso(compra, valor, politica.getPorcentaje());
            }
            reembolsos.add(cerrarReembolso(compra, valor, politica, ahora, TipoEvento.FUNCION_CANCELADA));
        }
        return reembolsos;
    }

    private Reembolso cerrarReembolso(Compra compra, double valor, PoliticaReembolso politica,
                                      LocalDateTime ahora, TipoEvento tipoEvento) {
        servicioAsientos.liberar(compra);
        Reembolso reembolso = new Reembolso(repositorio.siguienteId(), compra, valor, politica.getPorcentaje(),
                politica.getDescripcion(), ahora);
        compra.reembolsar(reembolso);
        repositorio.getReembolsos().add(reembolso);
        notificador.notificar(new EventoCompra(tipoEvento, compra, valor));
        return reembolso;
    }
}
