package co.edu.uniquindio.cinemauq.vista;

import co.edu.uniquindio.cinemauq.App;
import co.edu.uniquindio.cinemauq.controlador.LoginControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Vista de inicio de sesión (RF-002). Según el rol abre la vista del cliente o la del administrador. */
public class LoginVista {

    private final VBox raiz = new VBox(12);
    private final LoginControlador controlador = new LoginControlador(App.getSistema());

    public LoginVista() {
        Label titulo = new Label("CinemaUQ");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");
        txtUsuario.setMaxWidth(260);
        PasswordField txtClave = new PasswordField();
        txtClave.setPromptText("Contraseña");
        txtClave.setMaxWidth(260);
        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setDefaultButton(true);
        btnIngresar.setPrefWidth(260);
        Button btnRegistro = new Button("Registrarme");
        btnRegistro.setPrefWidth(260);
        Label ayuda = new Label("Demo: admin/admin123 · laura/1234 · andres/1234 · mateo/1234");
        ayuda.setStyle("-fx-text-fill: #666;");

        btnIngresar.setOnAction(e -> {
            try {
                abrirSesion(controlador.iniciarSesion(txtUsuario.getText(), txtClave.getText()));
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        btnRegistro.setOnAction(e -> App.mostrar(new RegistroVista(controlador).getRaiz(), "CinemaUQ - Registro"));

        raiz.getChildren().addAll(titulo, new Label("Sistema integral de gestión de cine"),
                txtUsuario, txtClave, btnIngresar, btnRegistro, ayuda);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(30));
    }

    /** Polimorfismo: el rol del Usuario decide qué pantalla se abre. */
    private void abrirSesion(Usuario usuario) {
        if (usuario.esAdministrador()) {
            App.mostrar(new AdminVista(usuario).getRaiz(), "CinemaUQ - Administrador");
        } else {
            App.mostrar(new ClienteVista((Cliente) usuario).getRaiz(), "CinemaUQ - Cliente: " + usuario.getNombre());
        }
    }

    public Parent getRaiz() {
        return raiz;
    }
}
