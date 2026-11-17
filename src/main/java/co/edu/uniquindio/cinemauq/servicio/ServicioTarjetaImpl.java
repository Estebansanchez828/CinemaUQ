package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;

import java.time.LocalDateTime;
import java.util.List;

/** Implementación de las operaciones de la tarjeta virtual. */
public class ServicioTarjetaImpl implements ServicioTarjeta {

    private final RepositorioCinema repositorio;

    public ServicioTarjetaImpl(RepositorioCinema repositorio) {
        this.repositorio = repositorio;
    }

    /** RN-03: tarjeta activa y saldo suficiente antes de confirmar. */
    @Override
    public void verificarPago(Cliente cliente, double valor) {
        cliente.getTarjeta().verificarPago(valor);
    }

    /** RN-04: descuenta el valor y registra el movimiento. */
    @Override
    public void cobrar(Compra compra) {
        compra.getCliente().getTarjeta().descontar(compra.getTotal(),
                "Compra " + compra.getCodigo(), LocalDateTime.now());
    }

    /** RN-10: el reembolso vuelve a la tarjeta usada en la compra. */
    @Override
    public void abonarReembolso(Compra compra, double valor, int porcentaje) {
        compra.getCliente().getTarjeta().abonarReembolso(valor,
                "Reembolso " + compra.getCodigo() + " (" + porcentaje + " %)", LocalDateTime.now());
    }

    /** RN-21: queda pendiente hasta que el administrador la atienda. */
    @Override
    public SolicitudRecarga solicitarRecarga(Cliente cliente, double valor) {
        if (valor <= 0) {
            throw new DatoInvalidoException("El valor de la recarga debe ser mayor que 0.");
        }
        SolicitudRecarga solicitud = new SolicitudRecarga(repositorio.siguienteId(), cliente, valor,
                LocalDateTime.now());
        repositorio.getSolicitudes().add(solicitud);
        return solicitud;
    }

    @Override
    public List<SolicitudRecarga> solicitudesDe(Cliente cliente) {
        return repositorio.getSolicitudes().stream().filter(s -> s.getCliente() == cliente).toList();
    }
}
