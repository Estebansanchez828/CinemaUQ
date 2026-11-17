package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.controlador.ReporteControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.reporte.Reporte;
import co.edu.uniquindio.cinemauq.vista.Alertas;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.nio.file.Path;
import java.time.LocalDate;

/** Generación de reportes con tipo y rango de fechas, y exportación a TXT (RF-026, RF-027). */
public class ReportesPestana extends PestanaBase {

    public ReportesPestana(AdminControlador controlador, ReporteControlador reportes) {
        super("Reportes", controlador);
        ComboBox<Reporte> cbTipo = new ComboBox<>(FXCollections.observableArrayList(reportes.tipos()));
        cbTipo.setPromptText("Tipo de reporte");
        cbTipo.setValue(reportes.tipos().get(0));
        DatePicker dpDesde = new DatePicker(LocalDate.now().minusMonths(6));
        DatePicker dpHasta = new DatePicker(LocalDate.now().plusDays(7));
        TextArea txtReporte = new TextArea();
        txtReporte.setEditable(false);
        txtReporte.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 12px;");
        Button btnGenerar = new Button("Generar");
        btnGenerar.setOnAction(e -> {
            try {
                txtReporte.setText(reportes.generar(cbTipo.getValue(), dpDesde.getValue(), dpHasta.getValue()));
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        Button btnExportar = new Button("Exportar TXT");
        btnExportar.setOnAction(e -> {
            try {
                Path archivo = reportes.exportar();
                Alertas.info("Reporte guardado en:\n" + archivo);
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        VBox contenido = panel(titulo("Reportes operativos"),
                new HBox(8, cbTipo, new Label("Desde"), dpDesde, new Label("Hasta"), dpHasta, btnGenerar, btnExportar),
                txtReporte);
        VBox.setVgrow(txtReporte, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        // El reporte se genera a pedido con el botón "Generar".
    }
}
