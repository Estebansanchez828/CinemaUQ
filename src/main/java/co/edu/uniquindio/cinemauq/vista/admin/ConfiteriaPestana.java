package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/** Gestión de productos y combos (RF-018, RF-019). Un combo puede contener otros combos (Composite). */
public class ConfiteriaPestana extends PestanaBase {

    private final TableView<ItemConfiteria> tabla = Tablas.crear();
    private final ListView<ItemConfiteria> listaCombo = new ListView<>();

    public ConfiteriaPestana(AdminControlador controlador) {
        super("Confitería y combos", controlador);
        Tablas.columna(tabla, "Id", i -> String.valueOf(i.getId()));
        Tablas.columna(tabla, "Nombre", ItemConfiteria::getNombre);
        Tablas.columna(tabla, "Tipo", ItemConfiteria::getTipo);
        Tablas.columna(tabla, "Categoría / contenido", ItemConfiteria::getDescripcion);
        Tablas.columna(tabla, "Precio", i -> Formato.dinero(i.getPrecio()));

        TextField txtNombre = campo("Nombre del producto", 180);
        TextField txtCategoria = campo("Categoría", 120);
        TextField txtPrecio = campo("Precio", 100);
        Button btnProducto = new Button("Crear producto");
        btnProducto.setOnAction(e -> ejecutar(() -> controlador.crearProducto(txtNombre.getText(),
                txtCategoria.getText(), txtPrecio.getText()), "Producto creado."));
        Button btnEliminar = new Button("Eliminar seleccionado");
        btnEliminar.setOnAction(e -> ejecutar(() -> controlador.eliminarItem(
                tabla.getSelectionModel().getSelectedItem()), "Ítem eliminado."));

        TextField txtCombo = campo("Nombre del combo", 180);
        TextField txtDescuento = campo("Descuento %", 100);
        Button btnAgregar = new Button("Agregar seleccionado al combo");
        btnAgregar.setOnAction(e -> {
            ItemConfiteria item = tabla.getSelectionModel().getSelectedItem();
            if (item != null) {
                listaCombo.getItems().add(item);
            }
        });
        Button btnLimpiar = new Button("Vaciar");
        btnLimpiar.setOnAction(e -> listaCombo.getItems().clear());
        Button btnCombo = new Button("Crear combo");
        btnCombo.setOnAction(e -> ejecutar(() -> {
            controlador.crearCombo(txtCombo.getText(), txtDescuento.getText(), new ArrayList<>(listaCombo.getItems()));
            listaCombo.getItems().clear();
        }, "Combo creado."));
        listaCombo.setPrefHeight(110);

        VBox contenido = panel(titulo("Productos y combos"), tabla, btnEliminar,
                titulo("Nuevo producto"), new HBox(8, txtNombre, txtCategoria, txtPrecio, btnProducto),
                titulo("Nuevo combo (productos u otros combos de la tabla)"),
                new HBox(8, txtCombo, txtDescuento, btnAgregar, btnLimpiar, btnCombo), listaCombo);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.confiteria());
    }
}
