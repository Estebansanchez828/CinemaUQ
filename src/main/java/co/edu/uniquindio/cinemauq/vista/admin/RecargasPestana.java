package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Atención de solicitudes de recarga (RF-024). Solo el administrador recarga (Proxy). */
public class RecargasPestana extends PestanaBase {

    private final TableView<SolicitudRecarga> tablaPendientes = Tablas.crear();
    private final TableView<SolicitudRecarga> tablaHistorial = Tablas.crear();

    public RecargasPestana(AdminControlador controlador) {
        super("Solicitudes de recarga", controlador);
        for (TableView<SolicitudRecarga> tabla : java.util.List.of(tablaPendientes, tablaHistorial)) {
            Tablas.columna(tabla, "Id", s -> String.valueOf(s.getId()));
            Tablas.columna(tabla, "Fecha", s -> Formato.fechaHora(s.getFecha()));
            Tablas.columna(tabla, "Cliente", s -> s.getCliente().getNombre());
            Tablas.columna(tabla, "Valor", s -> Formato.dinero(s.getValor()));
            Tablas.columna(tabla, "Estado", s -> s.getEstado().toString());
            Tablas.columna(tabla, "Saldo actual", s -> Formato.dinero(s.getCliente().getTarjeta().getSaldo()));
        }
        Button btnAprobar = new Button("Aprobar y recargar");
        btnAprobar.setStyle("-fx-base: #43a047;");
        btnAprobar.setOnAction(e -> ejecutar(() -> controlador.aprobar(
                tablaPendientes.getSelectionModel().getSelectedItem()), "Recarga aprobada y registrada."));
        Button btnRechazar = new Button("Rechazar");
        btnRechazar.setOnAction(e -> ejecutar(() -> controlador.rechazar(
                tablaPendientes.getSelectionModel().getSelectedItem()), "Solicitud rechazada."));
        VBox contenido = panel(titulo("Pendientes"), tablaPendientes, new HBox(8, btnAprobar, btnRechazar),
                titulo("Historial de solicitudes"), tablaHistorial);
        VBox.setVgrow(tablaPendientes, Priority.ALWAYS);
        VBox.setVgrow(tablaHistorial, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tablaPendientes, controlador.solicitudesPendientes());
        Tablas.llenar(tablaHistorial, controlador.todasLasSolicitudes());
    }
}
