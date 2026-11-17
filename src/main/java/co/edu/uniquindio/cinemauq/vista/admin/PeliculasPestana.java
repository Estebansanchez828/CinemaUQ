package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Gestión de películas (RF-015). */
public class PeliculasPestana extends PestanaBase {

    private final TableView<Pelicula> tabla = Tablas.crear();

    public PeliculasPestana(AdminControlador controlador) {
        super("Películas", controlador);
        Tablas.columna(tabla, "Id", p -> String.valueOf(p.getId()));
        Tablas.columna(tabla, "Título", Pelicula::getTitulo);
        Tablas.columna(tabla, "Género", Pelicula::getGenero);
        Tablas.columna(tabla, "Duración (min)", p -> String.valueOf(p.getDuracionMinutos()));
        Tablas.columna(tabla, "Clasificación", Pelicula::getClasificacion);

        TextField txtTitulo = campo("Título", 200);
        TextField txtGenero = campo("Género", 130);
        TextField txtDuracion = campo("Duración (min)", 110);
        TextField txtClasificacion = campo("Clasificación", 110);
        Button btnCrear = new Button("Crear película");
        btnCrear.setOnAction(e -> ejecutar(() -> controlador.crearPelicula(txtTitulo.getText(), txtGenero.getText(),
                txtDuracion.getText(), txtClasificacion.getText()), "Película creada."));
        Button btnEliminar = new Button("Eliminar seleccionada");
        btnEliminar.setOnAction(e -> ejecutar(() -> controlador.eliminarPelicula(
                tabla.getSelectionModel().getSelectedItem()), "Película eliminada."));

        VBox contenido = panel(titulo("Películas"), tabla,
                new HBox(8, txtTitulo, txtGenero, txtDuracion, txtClasificacion, btnCrear), btnEliminar);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.peliculas());
    }
}
