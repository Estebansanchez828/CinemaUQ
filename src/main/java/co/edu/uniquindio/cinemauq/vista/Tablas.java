package co.edu.uniquindio.cinemauq.vista;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.function.Function;

/** Ayudas para crear tablas de solo lectura en pocas líneas. */
public final class Tablas {

    private Tablas() {
    }

    public static <T> TableView<T> crear() {
        TableView<T> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("Sin registros"));
        return tabla;
    }

    public static <T> void columna(TableView<T> tabla, String titulo, Function<T, String> valor) {
        TableColumn<T, String> columna = new TableColumn<>(titulo);
        columna.setCellValueFactory(c -> new ReadOnlyStringWrapper(valor.apply(c.getValue())));
        tabla.getColumns().add(columna);
    }

    public static <T> void llenar(TableView<T> tabla, List<T> datos) {
        tabla.setItems(FXCollections.observableArrayList(datos));
    }
}
