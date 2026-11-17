package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/** Gestión de salas y asientos (RF-016). */
public class SalasPestana extends PestanaBase {

    private final TableView<Sala> tablaSalas = Tablas.crear();
    private final TableView<Asiento> tablaAsientos = Tablas.crear();

    public SalasPestana(AdminControlador controlador) {
        super("Salas y asientos", controlador);
        Tablas.columna(tablaSalas, "Id", s -> String.valueOf(s.getId()));
        Tablas.columna(tablaSalas, "Nombre", Sala::getNombre);
        Tablas.columna(tablaSalas, "Filas", s -> String.valueOf(s.getFilas()));
        Tablas.columna(tablaSalas, "Columnas", s -> String.valueOf(s.getColumnas()));
        Tablas.columna(tablaSalas, "Capacidad", s -> String.valueOf(s.getCapacidad()));
        tablaSalas.getSelectionModel().selectedItemProperty().addListener((obs, vieja, nueva) -> mostrarAsientos(nueva));

        Tablas.columna(tablaAsientos, "Asiento", Asiento::getCodigo);
        Tablas.columna(tablaAsientos, "Tipo", a -> a.getTipo().toString());

        TextField txtNombre = campo("Nombre", 160);
        TextField txtFilas = campo("Filas (1-15)", 100);
        TextField txtColumnas = campo("Columnas (1-20)", 110);
        Button btnCrear = new Button("Crear sala");
        btnCrear.setOnAction(e -> ejecutar(() -> controlador.crearSala(txtNombre.getText(), txtFilas.getText(),
                txtColumnas.getText()), "Sala creada. Fila A: asientos 1 y 2 accesibles; dos últimas filas: preferenciales."));
        Button btnEliminar = new Button("Eliminar sala seleccionada");
        btnEliminar.setOnAction(e -> ejecutar(() -> controlador.eliminarSala(
                tablaSalas.getSelectionModel().getSelectedItem()), "Sala eliminada."));

        ComboBox<TipoAsiento> cbTipo = new ComboBox<>(FXCollections.observableArrayList(TipoAsiento.values()));
        cbTipo.setPromptText("Nuevo tipo");
        Button btnCambiar = new Button("Cambiar tipo del asiento seleccionado");
        btnCambiar.setOnAction(e -> {
            Asiento asiento = tablaAsientos.getSelectionModel().getSelectedItem();
            ejecutar(() -> controlador.cambiarTipoAsiento(tablaSalas.getSelectionModel().getSelectedItem(),
                    asiento == null ? "" : asiento.getCodigo(), cbTipo.getValue()), null);
        });

        VBox izquierda = panel(titulo("Salas"), tablaSalas, new HBox(8, txtNombre, txtFilas, txtColumnas, btnCrear),
                btnEliminar);
        VBox derecha = panel(titulo("Asientos de la sala seleccionada"), tablaAsientos, new HBox(8, cbTipo, btnCambiar),
                new Label("El precio de la entrada depende del tipo: preferencial +25 %, accesible -20 %."));
        VBox.setVgrow(tablaSalas, Priority.ALWAYS);
        VBox.setVgrow(tablaAsientos, Priority.ALWAYS);
        HBox.setHgrow(izquierda, Priority.ALWAYS);
        HBox.setHgrow(derecha, Priority.ALWAYS);
        setContenido(new HBox(10, izquierda, derecha));
    }

    private void mostrarAsientos(Sala sala) {
        Tablas.llenar(tablaAsientos, sala == null ? List.of() : sala.getAsientos());
    }

    @Override
    public void refrescar() {
        Sala actual = tablaSalas.getSelectionModel().getSelectedItem();
        Tablas.llenar(tablaSalas, controlador.salas());
        if (actual != null && controlador.salas().contains(actual)) {
            tablaSalas.getSelectionModel().select(actual);
        }
    }
}
