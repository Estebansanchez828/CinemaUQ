package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.servicio.ServicioEstadisticas;
import co.edu.uniquindio.cinemauq.servicio.evento.ObservadorEstadisticas;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.vista.Tablas;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Estadísticas básicas en pantalla (RF-025). */
public class EstadisticasPestana extends PestanaBase {

    private final Label lblResumen = new Label();
    private final Label lblSesion = new Label();
    private final TableView<String[]> tablaPeliculas = tabla("Película", "Entradas vendidas");
    private final TableView<String[]> tablaProductos = tabla("Producto / combo", "Unidades");
    private final TableView<String[]> tablaOcupacion = tabla("Sala", "Ocupación");
    private final ListView<String> bitacora = new ListView<>();

    public EstadisticasPestana(AdminControlador controlador) {
        super("Estadísticas", controlador);
        lblResumen.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        HBox tablas = new HBox(10,
                panel(titulo("Películas con más entradas"), tablaPeliculas),
                panel(titulo("Productos más vendidos"), tablaProductos),
                panel(titulo("Ocupación de salas"), tablaOcupacion));
        bitacora.setPrefHeight(140);
        VBox contenido = panel(titulo("Resumen general"), lblResumen, tablas,
                titulo("Actividad de esta sesión (ObservadorEstadisticas)"), lblSesion, bitacora);
        VBox.setVgrow(tablas, Priority.ALWAYS);
        setContenido(contenido);
    }

    private static TableView<String[]> tabla(String columna1, String columna2) {
        TableView<String[]> tabla = Tablas.crear();
        Tablas.columna(tabla, columna1, fila -> fila[0]);
        Tablas.columna(tabla, columna2, fila -> fila[1]);
        tabla.setPrefWidth(360);
        return tabla;
    }

    private static <V> List<String[]> filas(Map<String, V> mapa, java.util.function.Function<V, String> formato) {
        List<String[]> filas = new ArrayList<>();
        mapa.forEach((clave, valor) -> filas.add(new String[]{clave, formato.apply(valor)}));
        return filas;
    }

    @Override
    public void refrescar() {
        ServicioEstadisticas e = controlador.estadisticas();
        lblResumen.setText("Ventas realizadas: " + e.ventasRealizadas()
                + "   |   Ingresos netos: " + Formato.dinero(e.ingresosNetos())
                + "   |   Reembolsos realizados: " + e.reembolsosRealizados()
                + " (" + Formato.dinero(e.valorReembolsado()) + ")");
        Tablas.llenar(tablaPeliculas, filas(e.entradasPorPelicula(), String::valueOf));
        Tablas.llenar(tablaProductos, filas(e.productosMasVendidos(), String::valueOf));
        Tablas.llenar(tablaOcupacion, filas(e.ocupacionPorSala(), v -> String.format("%.1f %%", v)));
        ObservadorEstadisticas sesion = controlador.actividadSesion();
        lblSesion.setText("Ventas: " + sesion.getVentas() + " (" + Formato.dinero(sesion.getIngresos()) + ")   |   Reembolsos: "
                + sesion.getReembolsos() + " (" + Formato.dinero(sesion.getValorReembolsado()) + ")");
        bitacora.setItems(FXCollections.observableArrayList(sesion.getBitacora()));
    }
}
