package co.edu.uniquindio.cinemauq.vista;

import co.edu.uniquindio.cinemauq.App;
import co.edu.uniquindio.cinemauq.controlador.LoginControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Vista de registro de clientes (RF-001). */
public class RegistroVista {

    private final VBox raiz = new VBox(12);

    public RegistroVista(LoginControlador controlador) {
        Label titulo = new Label("Registro de cliente");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        TextField txtNombre = campo("Nombre completo");
        TextField txtUsuario = campo("Usuario");
        PasswordField txtClave = new PasswordField();
        txtClave.setPromptText("Contraseña (mínimo 4 caracteres)");
        txtClave.setMaxWidth(300);
        PasswordField txtConfirmacion = new PasswordField();
        txtConfirmacion.setPromptText("Confirmar contraseña");
        txtConfirmacion.setMaxWidth(300);
        Button btnRegistrar = new Button("Registrarme");
        btnRegistrar.setPrefWidth(300);
        Button btnVolver = new Button("Volver");
        btnVolver.setPrefWidth(300);

        btnRegistrar.setOnAction(e -> {
            try {
                Cliente cliente = controlador.registrar(txtNombre.getText(), txtUsuario.getText(),
                        txtClave.getText(), txtConfirmacion.getText());
                Alertas.info("Registro exitoso. Su tarjeta virtual es " + cliente.getTarjeta().getNumero()
                        + " (saldo $0). Ya puede iniciar sesión.");
                App.mostrar(new LoginVista().getRaiz(), "CinemaUQ - Iniciar sesión");
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        btnVolver.setOnAction(e -> App.mostrar(new LoginVista().getRaiz(), "CinemaUQ - Iniciar sesión"));

        raiz.getChildren().addAll(titulo, txtNombre, txtUsuario, txtClave, txtConfirmacion, btnRegistrar, btnVolver);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(30));
    }

    private TextField campo(String texto) {
        TextField campo = new TextField();
        campo.setPromptText(texto);
        campo.setMaxWidth(300);
        return campo;
    }

    public Parent getRaiz() {
        return raiz;
    }
}
