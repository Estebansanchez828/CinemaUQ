package co.edu.uniquindio.cinemauq.servicio;

import java.util.Map;

/** Estadísticas básicas del cine (RF-025) y datos para las gráficas (RF-028). SOLID – ISP. */
public interface ServicioEstadisticas {

    int ventasRealizadas();

    double ingresosNetos();

    int reembolsosRealizados();

    double valorReembolsado();

    Map<String, Double> ocupacionPorSala();

    Map<String, Integer> entradasPorPelicula();

    Map<String, Integer> productosMasVendidos();

    Map<String, Integer> entradasPorSala();

    Map<String, Double> ventasPorCategoria();

    Map<String, Double> ventasPorMes();

    Map<String, Double> recargasPorMes();
}
