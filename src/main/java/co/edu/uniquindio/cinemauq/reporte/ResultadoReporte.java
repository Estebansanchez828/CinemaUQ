package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Datos de un reporte: título, fecha de generación, periodo, registros y totales. */
public class ResultadoReporte {

    private final String titulo;
    private final LocalDate desde;
    private final LocalDate hasta;
    private final LocalDateTime fechaGeneracion = LocalDateTime.now();
    private final String encabezado;
    private final List<String> registros = new ArrayList<>();
    private final List<String> totales = new ArrayList<>();

    public ResultadoReporte(String titulo, LocalDate desde, LocalDate hasta, String encabezado) {
        this.titulo = titulo;
        this.desde = desde;
        this.hasta = hasta;
        this.encabezado = encabezado;
    }

    public void agregarRegistro(String linea) {
        registros.add(linea);
    }

    public void agregarTotal(String linea) {
        totales.add(linea);
    }

    public String getPeriodo() {
        return Formato.fecha(desde) + " a " + Formato.fecha(hasta);
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public String getEncabezado() {
        return encabezado;
    }

    public List<String> getRegistros() {
        return registros;
    }

    public List<String> getTotales() {
        return totales;
    }

    /** Fila con columnas de ancho fijo para que el TXT quede alineado. */
    public static String fila(Object... celdas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < celdas.length; i += 2) {
            String texto = String.valueOf(celdas[i]);
            int ancho = (Integer) celdas[i + 1];
            if (texto.length() > ancho - 1) {
                texto = texto.substring(0, ancho - 2) + "…";
            }
            sb.append(String.format("%-" + ancho + "s", texto));
        }
        return sb.toString().stripTrailing();
    }

    /** true si la fecha está dentro del periodo (incluye los extremos). */
    public static boolean enRango(LocalDate fecha, LocalDate desde, LocalDate hasta) {
        return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }
}
