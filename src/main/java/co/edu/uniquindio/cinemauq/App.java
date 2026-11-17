package co.edu.uniquindio.cinemauq;

import co.edu.uniquindio.cinemauq.servicio.Sistema;
import co.edu.uniquindio.cinemauq.vista.LoginVista;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Aplicación JavaFX. Crea el Sistema (datos iniciales + servicios) una sola vez,
 * guarda la ventana principal y permite cambiar de pantalla.
 */
public class App extends Application {

    private static Stage escenario;
    private static Sistema sistema;

    @Override
    public void start(Stage stage) {
        escenario = stage;
        sistema = new Sistema();
        mostrar(new LoginVista().getRaiz(), "CinemaUQ - Iniciar sesión");
        stage.show();
    }

    /** Cambia la pantalla que se muestra en la ventana principal. */
    public static void mostrar(Parent raiz, String titulo) {
        escenario.setTitle(titulo);
        escenario.setScene(new Scene(raiz, 1200, 700));
    }

    public static Sistema getSistema() {
        return sistema;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
