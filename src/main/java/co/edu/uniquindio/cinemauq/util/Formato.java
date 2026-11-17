package co.edu.uniquindio.cinemauq.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Utilidad para mostrar dinero y fechas siempre con el mismo formato. */
public final class Formato {

    private static final Locale CO = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Formato() {
    }

    public static String dinero(double valor) {
        return String.format(CO, "$%,.0f", valor);
    }

    public static String fechaHora(LocalDateTime fecha) {
        return fecha == null ? "" : fecha.format(FECHA_HORA);
    }

    public static String fecha(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FECHA);
    }
}
