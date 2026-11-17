package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.vista.Alertas;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Clase abstracta con lo común de las pestañas: el controlador, la pestaña y el manejo de errores. */
public abstract class PestanaBase implements PestanaAdmin {

    protected final AdminControlador controlador;
    private final Tab tab;

    protected PestanaBase(String titulo, AdminControlador controlador) {
        this.controlador = controlador;
        this.tab = new Tab(titulo);
        this.tab.setClosable(false);
    }

    @Override
    public Tab getTab() {
        return tab;
    }

    protected void setContenido(Node contenido) {
        tab.setContent(contenido);
    }

    /** Ejecuta una acción; si el dominio lanza una excepción, la muestra en una alerta. */
    protected void ejecutar(Runnable accion, String mensajeExito) {
        try {
            accion.run();
            if (mensajeExito != null) {
                Alertas.info(mensajeExito);
            }
            refrescar();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    protected static TextField campo(String texto, double ancho) {
        TextField campo = new TextField();
        campo.setPromptText(texto);
        campo.setPrefWidth(ancho);
        return campo;
    }

    protected static Label titulo(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        return label;
    }

    protected static VBox panel(Node... hijos) {
        VBox caja = new VBox(10, hijos);
        caja.setPadding(new Insets(10));
        return caja;
    }
}
