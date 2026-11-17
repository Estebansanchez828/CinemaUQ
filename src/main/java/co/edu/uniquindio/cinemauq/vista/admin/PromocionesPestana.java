package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Gestión de promociones (RF-020). Las activas se aplican con el patrón Decorator. */
public class PromocionesPestana extends PestanaBase {

    private final TableView<Promocion> tabla = Tablas.crear();

    public PromocionesPestana(AdminControlador controlador) {
        super("Promociones", controlador);
        Tablas.columna(tabla, "Id", p -> String.valueOf(p.getId()));
        Tablas.columna(tabla, "Nombre", Promocion::getNombre);
        Tablas.columna(tabla, "Tipo", p -> p.getTipo().toString());
        Tablas.columna(tabla, "Valor", p -> p.getTipo() == TipoPromocion.PORCENTAJE
                ? (int) p.getValor() + " %" : Formato.dinero(p.getValor()));
        Tablas.columna(tabla, "Aplica a", p -> p.getAlcance().toString());
        Tablas.columna(tabla, "Activa", p -> p.isActiva() ? "Sí" : "No");

        TextField txtNombre = campo("Nombre", 180);
        ComboBox<TipoPromocion> cbTipo = new ComboBox<>(FXCollections.observableArrayList(TipoPromocion.values()));
        cbTipo.setPromptText("Tipo");
        TextField txtValor = campo("Valor (% o $)", 110);
        ComboBox<AlcancePromocion> cbAlcance = new ComboBox<>(FXCollections.observableArrayList(AlcancePromocion.values()));
        cbAlcance.setPromptText("Aplica a");
        Button btnCrear = new Button("Crear promoción");
        btnCrear.setOnAction(e -> ejecutar(() -> controlador.crearPromocion(txtNombre.getText(), cbTipo.getValue(),
                txtValor.getText(), cbAlcance.getValue()), "Promoción creada y activa."));
        Button btnAlternar = new Button("Activar / desactivar");
        btnAlternar.setOnAction(e -> ejecutar(() -> controlador.alternarPromocion(
                tabla.getSelectionModel().getSelectedItem()), null));
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> ejecutar(() -> controlador.eliminarPromocion(
                tabla.getSelectionModel().getSelectedItem()), "Promoción eliminada."));

        VBox contenido = panel(titulo("Promociones (se apilan: primero las de %, luego las de valor fijo)"), tabla,
                new HBox(8, txtNombre, cbTipo, txtValor, cbAlcance, btnCrear), new HBox(8, btnAlternar, btnEliminar));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.promociones());
    }
}
