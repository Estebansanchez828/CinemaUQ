package co.edu.uniquindio.cinemauq.vista;

import co.edu.uniquindio.cinemauq.App;
import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.controlador.ReporteControlador;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.vista.admin.ClientesPestana;
import co.edu.uniquindio.cinemauq.vista.admin.ComprasPestana;
import co.edu.uniquindio.cinemauq.vista.admin.ConfiteriaPestana;
import co.edu.uniquindio.cinemauq.vista.admin.EstadisticasPestana;
import co.edu.uniquindio.cinemauq.vista.admin.FuncionesPestana;
import co.edu.uniquindio.cinemauq.vista.admin.GraficasPestana;
import co.edu.uniquindio.cinemauq.vista.admin.PeliculasPestana;
import co.edu.uniquindio.cinemauq.vista.admin.PestanaAdmin;
import co.edu.uniquindio.cinemauq.vista.admin.PromocionesPestana;
import co.edu.uniquindio.cinemauq.vista.admin.RecargasPestana;
import co.edu.uniquindio.cinemauq.vista.admin.ReportesPestana;
import co.edu.uniquindio.cinemauq.vista.admin.SalasPestana;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

/** Panel del administrador: una pestaña por cada funcionalidad (RF-015 a RF-028). */
public class AdminVista {

    private final BorderPane raiz = new BorderPane();

    public AdminVista(Usuario usuario) {
        AdminControlador controlador = new AdminControlador(App.getSistema(), usuario);
        List<PestanaAdmin> pestanas = List.of(
                new PeliculasPestana(controlador), new SalasPestana(controlador), new FuncionesPestana(controlador),
                new ConfiteriaPestana(controlador), new PromocionesPestana(controlador), new ClientesPestana(controlador),
                new ComprasPestana(controlador), new RecargasPestana(controlador), new EstadisticasPestana(controlador),
                new ReportesPestana(controlador, new ReporteControlador(App.getSistema())),
                new GraficasPestana(controlador));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        for (PestanaAdmin pestana : pestanas) {
            tabPane.getTabs().add(pestana.getTab());
            pestana.refrescar();
        }
        // Al cambiar de pestaña se recargan sus datos (polimorfismo: todas responden a refrescar()).
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nueva) -> {
            for (PestanaAdmin pestana : pestanas) {
                if (pestana.getTab() == nueva) {
                    pestana.refrescar();
                }
            }
        });

        Label lblUsuario = new Label("Administrador: " + usuario.getNombre());
        lblUsuario.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.setOnAction(e -> App.mostrar(new LoginVista().getRaiz(), "CinemaUQ - Iniciar sesión"));
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        HBox barra = new HBox(10, lblUsuario, espacio, btnSalir);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10));
        barra.setStyle("-fx-background-color: #eceff1;");

        raiz.setTop(barra);
        raiz.setCenter(tabPane);
    }

    public Parent getRaiz() {
        return raiz;
    }
}
