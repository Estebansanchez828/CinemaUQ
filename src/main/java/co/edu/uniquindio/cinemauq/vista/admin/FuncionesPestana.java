package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Alertas;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

/** Gestión de funciones (RF-017) y cancelación de funciones por el cine (RF-023). */
public class FuncionesPestana extends PestanaBase {

    private final TableView<Funcion> tabla = Tablas.crear();
    private final ComboBox<Pelicula> cbPelicula = new ComboBox<>();
    private final ComboBox<Sala> cbSala = new ComboBox<>();

    public FuncionesPestana(AdminControlador controlador) {
        super("Funciones", controlador);
        Tablas.columna(tabla, "Id", f -> String.valueOf(f.getId()));
        Tablas.columna(tabla, "Película", f -> f.getPelicula().getTitulo());
        Tablas.columna(tabla, "Sala", f -> f.getSala().getNombre());
        Tablas.columna(tabla, "Inicio", f -> Formato.fechaHora(f.getInicio()));
        Tablas.columna(tabla, "Fin (con aseo)", f -> Formato.fechaHora(f.getFin()));
        Tablas.columna(tabla, "Precio base", f -> Formato.dinero(f.getPrecioBase()));
        Tablas.columna(tabla, "Vendidos", f -> f.getAsientosOcupados().size() + " / " + f.getSala().getCapacidad());
        Tablas.columna(tabla, "Estado", Funcion::getEstadoTexto);

        cbPelicula.setPromptText("Película");
        cbSala.setPromptText("Sala");
        DatePicker dpFecha = new DatePicker(LocalDate.now().plusDays(1));
        TextField txtHora = campo("Hora HH:mm", 90);
        TextField txtPrecio = campo("Precio base", 100);
        Button btnCrear = new Button("Crear función");
        btnCrear.setOnAction(e -> ejecutar(() -> controlador.crearFuncion(cbPelicula.getValue(), cbSala.getValue(),
                dpFecha.getValue(), txtHora.getText(), txtPrecio.getText()), "Función creada."));
        Button btnEliminar = new Button("Eliminar (sin ventas)");
        btnEliminar.setOnAction(e -> ejecutar(() -> controlador.eliminarFuncion(
                tabla.getSelectionModel().getSelectedItem()), "Función eliminada."));
        Button btnCancelar = new Button("Cancelar función (reembolso 100 %)");
        btnCancelar.setStyle("-fx-base: #e53935;");
        btnCancelar.setOnAction(e -> cancelarFuncion());

        VBox contenido = panel(titulo("Funciones (no se pueden cruzar en la misma sala: duración + 15 min de aseo)"), tabla,
                new HBox(8, cbPelicula, cbSala, dpFecha, txtHora, txtPrecio, btnCrear),
                new HBox(8, btnEliminar, btnCancelar));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    private void cancelarFuncion() {
        Funcion funcion = tabla.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            Alertas.error("Seleccione una función.");
            return;
        }
        if (!Alertas.confirmar("¿Cancelar la función " + funcion + "? Se reembolsará el 100 % a todos los compradores.")) {
            return;
        }
        try {
            List<Reembolso> reembolsos = controlador.cancelarFuncion(funcion);
            double total = reembolsos.stream().mapToDouble(Reembolso::getValor).sum();
            Alertas.info("Función cancelada. Compras reembolsadas: " + reembolsos.size()
                    + ". Total devuelto a las tarjetas: " + Formato.dinero(total) + ".");
            refrescar();
        } catch (CinemaException ex) {
            Alertas.error(ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.funciones());
        cbPelicula.getItems().setAll(controlador.peliculas());
        cbSala.getItems().setAll(controlador.salas());
    }
}
