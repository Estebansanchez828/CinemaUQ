package co.edu.uniquindio.cinemauq.vista.admin;

import co.edu.uniquindio.cinemauq.controlador.AdminControlador;
import co.edu.uniquindio.cinemauq.servicio.ServicioEstadisticas;
import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/** Reportes visuales con gráficas de JavaFX: barras, pastel y líneas (RF-028). */
public class GraficasPestana extends PestanaBase {

    private static final double ANCHO = 560;
    private static final double ALTO = 330;

    public GraficasPestana(AdminControlador controlador) {
        super("Gráficas", controlador);
    }

    /** Se reconstruyen las gráficas cada vez para que muestren los datos actuales. */
    @Override
    public void refrescar() {
        ServicioEstadisticas e = controlador.estadisticas();
        Button btnActualizar = new Button("Actualizar gráficas");
        btnActualizar.setOnAction(ev -> refrescar());
        VBox contenido = panel(btnActualizar,
                new HBox(10, barras("Películas con más entradas vendidas", "Entradas", e.entradasPorPelicula()),
                        barras("Productos y combos más vendidos", "Unidades", e.productosMasVendidos())),
                new HBox(10, pastel("Distribución de ventas por categoría", e.ventasPorCategoria()),
                        pastel("Entradas vendidas por sala", convertir(e.entradasPorSala()))),
                lineas("Ventas y recargas por mes", e.ventasPorMes(), e.recargasPorMes()));
        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        setContenido(scroll);
    }

    private BarChart<String, Number> barras(String titulo, String eje, Map<String, Integer> datos) {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        y.setLabel(eje);
        BarChart<String, Number> grafica = new BarChart<>(x, y);
        grafica.setTitle(titulo);
        grafica.setLegendVisible(false);
        grafica.setAnimated(false);
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        datos.forEach((clave, valor) -> serie.getData().add(new XYChart.Data<>(clave, valor)));
        grafica.getData().add(serie);
        grafica.setPrefSize(ANCHO, ALTO);
        return grafica;
    }

    private PieChart pastel(String titulo, Map<String, Double> datos) {
        List<PieChart.Data> porciones = new ArrayList<>();
        double total = datos.values().stream().mapToDouble(Double::doubleValue).sum();
        datos.forEach((clave, valor) -> porciones.add(new PieChart.Data(clave + " ("
                + (total == 0 ? 0 : Math.round(valor * 100 / total)) + " %)", valor)));
        PieChart grafica = new PieChart(FXCollections.observableArrayList(porciones));
        grafica.setTitle(titulo);
        grafica.setAnimated(false);
        grafica.setPrefSize(ANCHO, ALTO);
        return grafica;
    }

    private LineChart<String, Number> lineas(String titulo, Map<String, Double> ventas, Map<String, Double> recargas) {
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Mes");
        NumberAxis y = new NumberAxis();
        y.setLabel("Pesos");
        LineChart<String, Number> grafica = new LineChart<>(x, y);
        grafica.setTitle(titulo);
        grafica.setAnimated(false);
        TreeSet<String> meses = new TreeSet<>(ventas.keySet());
        meses.addAll(recargas.keySet());
        XYChart.Series<String, Number> serieVentas = new XYChart.Series<>();
        serieVentas.setName("Ventas");
        XYChart.Series<String, Number> serieRecargas = new XYChart.Series<>();
        serieRecargas.setName("Recargas");
        for (String mes : meses) {
            serieVentas.getData().add(new XYChart.Data<>(mes, ventas.getOrDefault(mes, 0.0)));
            serieRecargas.getData().add(new XYChart.Data<>(mes, recargas.getOrDefault(mes, 0.0)));
        }
        grafica.getData().add(serieVentas);
        grafica.getData().add(serieRecargas);
        grafica.setPrefSize(ANCHO * 2 + 10, ALTO);
        return grafica;
    }

    private Map<String, Double> convertir(Map<String, Integer> datos) {
        Map<String, Double> resultado = new java.util.LinkedHashMap<>();
        datos.forEach((clave, valor) -> resultado.put(clave, valor.doubleValue()));
        return resultado;
    }
}
