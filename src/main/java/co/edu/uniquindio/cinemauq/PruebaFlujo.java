package co.edu.uniquindio.cinemauq;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Combo;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.LotePuntos;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.servicio.Sistema;
import co.edu.uniquindio.cinemauq.servicio.admin.ProxyAdmin;
import co.edu.uniquindio.cinemauq.servicio.fachada.Cotizacion;
import co.edu.uniquindio.cinemauq.servicio.fachada.FachadaCompra;
import co.edu.uniquindio.cinemauq.util.Formato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Prueba por consola de principio a fin: recorre las reglas de negocio y los 10 patrones.
 * Sirve de respaldo en la sustentación (clic derecho > Run 'PruebaFlujo.main()').
 */
public class PruebaFlujo {

    private static Sistema sistema;
    private static RepositorioCinema repo;

    public static void main(String[] args) {
        sistema = new Sistema();
        repo = sistema.getRepositorio();
        FachadaCompra fachada = sistema.getFachadaCompra();

        titulo("1. SINGLETON: una sola instancia del repositorio");
        System.out.println("¿Misma instancia? " + (RepositorioCinema.getInstancia() == repo));
        System.out.println("Películas: " + repo.getPeliculas().size() + " | Funciones: " + repo.getFunciones().size()
                + " | Compras históricas: " + repo.getCompras().size());

        Cliente laura = (Cliente) sistema.getServicioAutenticacion().iniciarSesion("laura", "1234");
        Cliente andres = (Cliente) sistema.getServicioAutenticacion().iniciarSesion("andres", "1234");
        Cliente mateo = (Cliente) sistema.getServicioAutenticacion().iniciarSesion("mateo", "1234");
        Usuario admin = sistema.getServicioAutenticacion().iniciarSesion("admin", "admin123");
        System.out.println("Sesiones: " + laura + ", " + andres + ", " + mateo + ", " + admin);

        titulo("2. COMPOSITE: un combo dentro de otro combo");
        for (Combo combo : repo.getCombos()) {
            System.out.println(combo.getNombre() + " = " + combo.getDescripcion() + " -> " + Formato.dinero(combo.getPrecio()));
        }

        Funcion f1 = funcion("El viaje del colibrí", "Sala 1");
        Funcion f2 = funcion("Sombras en el Quindío", "Sala 2");
        Funcion f3 = funcion("Galaxia 9", "Sala 1");
        Funcion f4 = funcion("Risas en familia", "Sala 3 VIP");
        Funcion f5 = funcion("El último tren", "Sala 2");

        titulo("3. FACTORY METHOD + DECORATOR: cotización con promociones");
        List<Asiento> asientos = List.of(f3.getSala().buscarAsiento("A1"), f3.getSala().buscarAsiento("C3"),
                f3.getSala().buscarAsiento("F8"));
        ItemConfiteria pareja = combo("Combo pareja");
        Cotizacion cotizacion = fachada.cotizar(f3, asientos, List.of(pareja));
        cotizacion.getDetalle().forEach(l -> System.out.println("  " + l));
        System.out.println("Subtotal " + Formato.dinero(cotizacion.getSubtotal()) + " - descuento "
                + Formato.dinero(cotizacion.getDescuento()) + " = " + Formato.dinero(cotizacion.getTotal()));

        titulo("4. FACADE + BUILDER + STATE + OBSERVER: compra con tarjeta");
        double saldoAntes = laura.getTarjeta().getSaldo();
        Compra compra = fachada.comprar(laura, f3, asientos, List.of(pareja), MetodoPago.TARJETA);
        System.out.println("Compra " + compra.getCodigo() + " | estado: " + compra.getNombreEstado()
                + " | total " + Formato.dinero(compra.getTotal()));
        System.out.println("Saldo: " + Formato.dinero(saldoAntes) + " -> " + Formato.dinero(laura.getTarjeta().getSaldo()));
        System.out.println("Observer -> puntos ganados: " + compra.getPuntosGanados()
                + " | notificación: " + laura.getNotificaciones().get(0).getMensaje());

        titulo("5. RN-02: el mismo asiento no se vende dos veces");
        intentar(() -> fachada.comprar(andres, f3, List.of(f3.getSala().buscarAsiento("C3")), List.of(), MetodoPago.TARJETA));

        titulo("6. RN-03: saldo insuficiente y tarjeta inactiva");
        intentar(() -> fachada.comprar(andres, f4, List.of(f4.getSala().buscarAsiento("B1"),
                f4.getSala().buscarAsiento("B2")), List.of(), MetodoPago.TARJETA));
        intentar(() -> fachada.comprar(mateo, f4, List.of(f4.getSala().buscarAsiento("B3")), List.of(), MetodoPago.TARJETA));

        titulo("7. PROXY: solo el administrador recarga (RN-05)");
        SolicitudRecarga solicitud = sistema.getServicioTarjeta().solicitarRecarga(andres, 30000);
        System.out.println("Andrés solicitó una recarga de " + Formato.dinero(solicitud.getValor()));
        ProxyAdmin proxyCliente = sistema.administracion(andres);
        intentar(() -> proxyCliente.recargarTarjeta(andres, 1000000));
        ProxyAdmin proxyAdmin = sistema.administracion(admin);
        proxyAdmin.aprobarSolicitud(solicitud);
        System.out.println("Admin aprobó. Nuevo saldo de Andrés: " + Formato.dinero(andres.getTarjeta().getSaldo())
                + " | último movimiento: " + ultimoMovimiento(andres));

        titulo("8. PUNTOS: insuficientes, redención y orden de uso (RN-12)");
        System.out.println("Andrés tiene " + sistema.getServicioPuntos().consultarSaldo(andres) + " puntos");
        intentar(() -> fachada.comprar(andres, f4, List.of(f4.getSala().buscarAsiento("B4"),
                f4.getSala().buscarAsiento("B5")), List.of(), MetodoPago.PUNTOS));
        System.out.println("Lotes de Laura ANTES:");
        imprimirLotes(laura);
        Compra redencion = fachada.comprar(laura, f4, List.of(f4.getSala().buscarAsiento("B6")), List.of(),
                MetodoPago.PUNTOS);
        System.out.println("Redención " + redencion.getCodigo() + " con " + redencion.getPuntosUsados()
                + " puntos (no gana puntos: " + redencion.getPuntosGanados() + ")");
        System.out.println("Lotes de Laura DESPUÉS (se usó primero el que vence antes; el vencido no se tocó):");
        imprimirLotes(laura);

        titulo("9. STRATEGY + STATE: cancelaciones según el tiempo");
        Compra compraF2 = compraDe(laura, f2);
        Compra compraF1 = compraDe(laura, f1);
        Reembolso r100 = fachada.cancelarCompra(compra);
        System.out.println(compra.getCodigo() + " (" + horas(f3) + " h): " + r100.getPorcentaje() + " % = "
                + Formato.dinero(r100.getValor()) + " | estado: " + compra.getNombreEstado());
        System.out.print("Cancelar otra vez " + compra.getCodigo() + " -> ");
        intentar(() -> fachada.cancelarCompra(compra));
        Reembolso r80 = fachada.cancelarCompra(compraF2);
        System.out.println(compraF2.getCodigo() + " (" + horas(f2) + " h): " + r80.getPorcentaje() + " % = "
                + Formato.dinero(r80.getValor()));
        System.out.print(compraF1.getCodigo() + " (" + horas(f1) + " h) -> ");
        intentar(() -> fachada.cancelarCompra(compraF1));
        System.out.print("Redención con puntos -> ");
        intentar(() -> fachada.cancelarCompra(redencion));

        titulo("10. CANCELACIÓN DE FUNCIÓN POR EL CINE (RN-09): 100 % a todos");
        List<Reembolso> reembolsos = proxyAdmin.cancelarFuncion(f5);
        for (Reembolso r : reembolsos) {
            System.out.println(r.getCompra().getCodigo() + " de " + r.getCompra().getCliente().getNombre() + ": "
                    + r.getPorcentaje() + " % = " + Formato.dinero(r.getValor()) + " | "
                    + r.getCompra().getCliente().getNotificaciones().get(0).getMensaje());
        }

        titulo("11. RN-14: funciones cruzadas en la misma sala");
        intentar(() -> proxyAdmin.crearFuncion(f3.getPelicula(), f3.getSala(), f3.getInicio().plusMinutes(30), 12000));

        titulo("12. OBSERVER de estadísticas (actividad de esta sesión)");
        System.out.println("Ventas: " + sistema.getObservadorEstadisticas().getVentas() + " | Ingresos: "
                + Formato.dinero(sistema.getObservadorEstadisticas().getIngresos()) + " | Reembolsos: "
                + sistema.getObservadorEstadisticas().getReembolsos());
        sistema.getObservadorEstadisticas().getBitacora().forEach(l -> System.out.println("  " + l));

        titulo("FIN: todas las reglas y los 10 patrones se ejecutaron");
    }

    // ----- Ayudas -----

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void intentar(Runnable accion) {
        try {
            accion.run();
            System.out.println("  (sin error)");
        } catch (CinemaException e) {
            System.out.println("  " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static Funcion funcion(String pelicula, String sala) {
        return repo.getFunciones().stream()
                .filter(f -> f.getPelicula().getTitulo().equals(pelicula) && f.getSala().getNombre().equals(sala)
                        && f.getInicio().isAfter(LocalDateTime.now()))
                .findFirst().orElseThrow();
    }

    private static ItemConfiteria combo(String nombre) {
        return repo.getConfiteria().stream().filter(i -> i.getNombre().equals(nombre)).findFirst().orElseThrow();
    }

    private static Compra compraDe(Cliente cliente, Funcion funcion) {
        return repo.comprasDe(cliente).stream().filter(c -> c.getFuncion() == funcion && c.estaPagada())
                .findFirst().orElseThrow();
    }

    private static String horas(Funcion funcion) {
        return String.format("%.1f", funcion.horasRestantes(LocalDateTime.now()));
    }

    private static String ultimoMovimiento(Cliente cliente) {
        var movimientos = cliente.getTarjeta().getMovimientos();
        var m = movimientos.get(movimientos.size() - 1);
        return m.getTipo() + " " + Formato.dinero(m.getValor());
    }

    private static void imprimirLotes(Cliente cliente) {
        LocalDate hoy = LocalDate.now();
        for (LotePuntos lote : cliente.getCuentaPuntos().getLotes()) {
            System.out.println("  " + lote.getPuntos() + " pts | disponibles " + lote.getDisponibles() + " | vence "
                    + Formato.fecha(lote.getFechaVencimiento()) + " | " + lote.getEstado(hoy));
        }
        System.out.println("  Saldo vigente: " + cliente.getCuentaPuntos().getSaldo(hoy));
    }
}
