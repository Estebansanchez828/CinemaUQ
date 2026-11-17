package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Consulta de clientes, estado de la tarjeta y recarga directa (RF-021, RF-024). */
public class ClientesPestana extends PestanaBase {

    private final TableView<Cliente> tabla = Tablas.crear();

    public ClientesPestana(AdminControlador controlador) {
        super("Clientes", controlador);
        Tablas.columna(tabla, "Nombre", Cliente::getNombre);
        Tablas.columna(tabla, "Usuario", Cliente::getUsuario);
        Tablas.columna(tabla, "Tarjeta", c -> c.getTarjeta().getNumero());
        Tablas.columna(tabla, "Estado tarjeta", c -> c.getTarjeta().isActiva() ? "Activa" : "Inactiva");
        Tablas.columna(tabla, "Saldo", c -> Formato.dinero(c.getTarjeta().getSaldo()));
        Tablas.columna(tabla, "Puntos vigentes", c -> String.valueOf(controlador.puntosVigentes(c)));
        Tablas.columna(tabla, "Movimientos", c -> String.valueOf(c.getTarjeta().getMovimientos().size()));

        Button btnEstado = new Button("Activar / bloquear tarjeta");
        btnEstado.setOnAction(e -> ejecutar(() -> controlador.alternarTarjeta(
                tabla.getSelectionModel().getSelectedItem()), null));
        TextField txtValor = campo("Valor de la recarga", 150);
        Button btnRecargar = new Button("Recargar tarjeta seleccionada");
        btnRecargar.setOnAction(e -> ejecutar(() -> controlador.recargar(
                tabla.getSelectionModel().getSelectedItem(), txtValor.getText()), "Recarga registrada en los movimientos."));

        VBox contenido = panel(titulo("Clientes"), tabla, new HBox(8, btnEstado, txtValor, btnRecargar));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        setContenido(contenido);
    }

    @Override
    public void refrescar() {
        Tablas.llenar(tabla, controlador.clientes());
    }
}
