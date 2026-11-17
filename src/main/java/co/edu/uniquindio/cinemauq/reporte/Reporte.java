package co.edu.uniquindio.cinemauq.reporte;

import java.time.LocalDate;

/**
 * Reporte operativo (RF-026). Cada reporte solo calcula sus datos; el formato lo pone ExportadorTxt.
 * SOLID – OCP: un reporte nuevo es una clase nueva que implementa esta interfaz.
 * SOLID – SRP: calcular (Reporte) está separado de escribir el archivo (ExportadorTxt).
 */
public interface Reporte {

    String getNombre();

    ResultadoReporte generar(LocalDate desde, LocalDate hasta);
}
