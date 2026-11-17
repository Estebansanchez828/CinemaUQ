package co.edu.uniquindio.cinemauq.reporte;

import co.edu.uniquindio.cinemauq.excepciones.OperacionNoPermitidaException;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;

/**
 * Da formato de texto a un reporte y lo guarda en la carpeta reportes/ (RF-027).
 * SOLID – SRP: su única responsabilidad es el formato y el archivo.
 */
public class ExportadorTxt {

    private static final String LINEA = "=".repeat(100);
    private static final String GUION = "-".repeat(100);

    public String formatear(ResultadoReporte resultado) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINEA).append('\n')
                .append("CINEMAUQ - ").append(resultado.getTitulo().toUpperCase()).append('\n')
                .append("Fecha de generación: ").append(Formato.fechaHora(resultado.getFechaGeneracion())).append('\n')
                .append("Periodo consultado:  ").append(resultado.getPeriodo()).append('\n')
                .append("Registros encontrados: ").append(resultado.getRegistros().size()).append('\n')
                .append(LINEA).append('\n')
                .append(resultado.getEncabezado()).append('\n')
                .append(GUION).append('\n');
        if (resultado.getRegistros().isEmpty()) {
            sb.append("(No hay registros en el periodo)\n");
        }
        resultado.getRegistros().forEach(r -> sb.append(r).append('\n'));
        sb.append(GUION).append('\n').append("TOTALES\n");
        resultado.getTotales().forEach(t -> sb.append("  ").append(t).append('\n'));
        sb.append(LINEA).append('\n');
        return sb.toString();
    }

    public Path exportar(ResultadoReporte resultado) {
        try {
            Path carpeta = Paths.get("reportes");
            Files.createDirectories(carpeta);
            String sinTildes = Normalizer.normalize(resultado.getTitulo(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
            String nombre = sinTildes.toLowerCase().replaceAll("[^a-z0-9]+", "_")
                    + "_" + resultado.getFechaGeneracion().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            Path archivo = carpeta.resolve(nombre);
            Files.writeString(archivo, formatear(resultado), StandardCharsets.UTF_8);
            return archivo.toAbsolutePath();
        } catch (IOException e) {
            throw new OperacionNoPermitidaException("No se pudo guardar el reporte: " + e.getMessage());
        }
    }
}
