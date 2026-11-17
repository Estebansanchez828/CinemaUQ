package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Consulta de todas las compras (RF-022). */
public class ComprasPestana extends PestanaBase {

    private final TableView<Compra> tabla = Tablas.crear();

    public ComprasPestana(AdminControlador controlador) {
        super("Compras", controlador);
        Tablas.columna(tabla, "Código", Compra::getCodigo);
        Tablas.columna(tabla, "Fecha", c -> Formato.fechaHora(c.getFecha()));
        Tablas.columna(tabla, "Cliente", c -> c.getCliente().getNombre());
        Tablas.columna(tabla, "Función", c -> c.getFuncion() == null ? "Solo confitería" : c.getFuncion().toString());
        Tablas.columna(tabla, "Detalle", Compra::getResumen);
        Tablas.columna(tabla, "Total", c -> Formato.dinero(c.getTotal()));
        Tablas.columna(tabla, "Pago", c -> c.getMetodoPago() == MetodoPago.TARJETA ? "Tarjeta" : c.getPuntosUsados() + " pts");
        Tablas.columna(tabla, "Estado", Compra::getNombreEstado);
        VBox contenido = panel(titulo("Todas las compras"), tabla);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.compras());
    }
}
