package co.edu.uniquindio.cinemauq.datos;

import co.edu.uniquindio.cinemauq.modelo.AlcancePromocion;
import co.edu.uniquindio.cinemauq.modelo.Administrador;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Combo;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Producto;
import co.edu.uniquindio.cinemauq.modelo.Promocion;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.ReglaPuntos;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.modelo.TipoPromocion;
import co.edu.uniquindio.cinemauq.servicio.constructor.ConstructorCompraEstandar;
import co.edu.uniquindio.cinemauq.servicio.constructor.DirectorCompra;
import co.edu.uniquindio.cinemauq.servicio.fabrica.CreadorEntrada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Datos iniciales para la demostración (RF-029).
 * Las fechas se calculan desde ahora para que la demo funcione cualquier día:
 * una función en menos de 2 h, otra entre 2 y 24 h y otras a más de 24 h.
 * Incluye compras, recargas y reembolsos de varios meses y lotes de puntos
 * vencidos y a punto de vencer.
 *
 * Usuarios: admin / admin123 · laura / 1234 · andres / 1234 · mateo / 1234 (tarjeta inactiva).
 */
public final class DatosIniciales {

    private static final DirectorCompra DIRECTOR = new DirectorCompra();

    private DatosIniciales() {
    }

    public static void cargar() {
        RepositorioCinema repo = RepositorioCinema.getInstancia();
        if (!repo.estaVacio()) {
            return;
        }
        LocalDateTime ahora = LocalDateTime.now().withSecond(0).withNano(0);

        // ----- Usuarios -----
        repo.getUsuarios().add(new Administrador("Administrador CinemaUQ", "admin", "admin123"));
        Cliente laura = cliente(repo, "Laura Gómez", "laura");
        Cliente andres = cliente(repo, "Andrés Ríos", "andres");
        Cliente mateo = cliente(repo, "Mateo López", "mateo");

        // ----- Películas -----
        Pelicula colibri = pelicula(repo, "El viaje del colibrí", "Animación", 95, "Todos");
        Pelicula sombras = pelicula(repo, "Sombras en el Quindío", "Suspenso", 118, "+15");
        Pelicula galaxia = pelicula(repo, "Galaxia 9", "Ciencia ficción", 130, "+12");
        Pelicula risas = pelicula(repo, "Risas en familia", "Comedia", 100, "Todos");
        Pelicula tren = pelicula(repo, "El último tren", "Drama", 110, "+12");

        // ----- Salas -----
        Sala sala1 = sala(repo, "Sala 1", 6, 10);
        Sala sala2 = sala(repo, "Sala 2", 5, 8);
        Sala sala3 = sala(repo, "Sala 3 VIP", 4, 6);

        // ----- Confitería (Composite: combos con productos y un combo dentro de otro) -----
        Producto crispetasMedianas = producto(repo, "Crispetas medianas", "Crispetas", 8000);
        Producto crispetasGrandes = producto(repo, "Crispetas grandes", "Crispetas", 10000);
        Producto gaseosa = producto(repo, "Gaseosa", "Bebidas", 6000);
        producto(repo, "Agua", "Bebidas", 4000);
        Producto nachos = producto(repo, "Nachos con queso", "Snacks", 10000);
        producto(repo, "Chocolatina", "Dulces", 4000);
        Combo personal = combo(repo, "Combo personal", 10, crispetasMedianas, gaseosa);
        Combo pareja = combo(repo, "Combo pareja", 25, crispetasGrandes, crispetasGrandes, gaseosa, gaseosa);
        Combo familiar = combo(repo, "Combo familiar", 10, pareja, nachos, gaseosa, gaseosa);

        // ----- Promociones -----
        promocion(repo, "Descuento estudiante UQ", TipoPromocion.PORCENTAJE, 10, AlcancePromocion.ENTRADAS, true);
        promocion(repo, "Bono confitería", TipoPromocion.VALOR_FIJO, 2000, AlcancePromocion.CONFITERIA, true);
        promocion(repo, "Black Friday", TipoPromocion.PORCENTAJE, 20, AlcancePromocion.TODO, false);

        // ----- Funciones futuras (para la demo) -----
        Funcion f1 = funcion(repo, colibri, sala1, ahora.plusMinutes(90), 12000);            // menos de 2 h
        Funcion f2 = funcion(repo, sombras, sala2, ahora.plusHours(6), 12000);              // entre 2 y 24 h
        Funcion f3 = funcion(repo, galaxia, sala1, ahora.plusDays(2).withHour(18).withMinute(0), 12000);
        funcion(repo, risas, sala3, ahora.plusDays(3).withHour(20).withMinute(30), 14000);
        Funcion f5 = funcion(repo, tren, sala2, ahora.plusHours(30), 12000);
        funcion(repo, colibri, sala3, ahora.plusDays(4).withHour(15).withMinute(0), 14000);

        // ----- Funciones pasadas (historial para reportes y gráficas) -----
        Funcion p1 = funcion(repo, tren, sala1, ahora.minusDays(395).withHour(19).withMinute(0), 11000);
        Funcion p2 = funcion(repo, sombras, sala2, ahora.minusDays(355).withHour(19).withMinute(0), 11000);
        Funcion p3 = funcion(repo, risas, sala3, ahora.minusDays(110).withHour(19).withMinute(0), 12000);
        Funcion p4 = funcion(repo, galaxia, sala1, ahora.minusDays(80).withHour(19).withMinute(0), 12000);
        Funcion p5 = funcion(repo, colibri, sala2, ahora.minusDays(50).withHour(19).withMinute(0), 12000);
        Funcion p6 = funcion(repo, sombras, sala3, ahora.minusDays(20).withHour(19).withMinute(0), 12000);
        Funcion p7 = funcion(repo, galaxia, sala1, ahora.minusDays(7).withHour(19).withMinute(0), 12000);

        // ----- Historial en orden cronológico -----
        recargaHistorica(repo, laura, 300000, ahora.minusDays(400));
        // Lote vencido (más de un año) y lote a punto de vencer (vence en 10 días aprox.)
        compraPagada(repo, laura, p1, List.of(pareja), ahora.minusDays(397), "C4", "C5");
        compraPagada(repo, laura, p2, List.of(familiar), ahora.minusDays(357), "D3", "D4");
        recargaHistorica(repo, andres, 120000, ahora.minusDays(125));
        compraPagada(repo, andres, p3, List.of(gaseosa, gaseosa), ahora.minusDays(112), "A1", "A2");
        compraPagada(repo, laura, p4, List.of(crispetasGrandes), ahora.minusDays(82), "B5", "B6", "B7");
        compraPagada(repo, andres, p4, List.of(nachos), ahora.minusDays(81), "C3");
        recargaHistorica(repo, laura, 150000, ahora.minusDays(60));
        Compra cancelada100 = compraPagada(repo, laura, p5, List.of(), ahora.minusDays(56), "C4", "C5");
        reembolsoHistorico(repo, cancelada100, 100, "Cancelación con más de 24 horas", ahora.minusDays(53));
        recargaHistorica(repo, mateo, 80000, ahora.minusDays(40));
        compraPagada(repo, laura, p6, List.of(gaseosa, gaseosa), ahora.minusDays(22), "B3", "B4");
        Compra cancelada80 = compraPagada(repo, andres, p6, List.of(), ahora.minusDays(21), "B5");
        reembolsoHistorico(repo, cancelada80, 80, "Cancelación entre 2 y 24 horas", p6.getInicio().minusHours(5));
        compraPagada(repo, andres, p7, List.of(), ahora.minusDays(8), "D1", "D2", "D3", "D4");
        compraPagada(repo, mateo, p7, List.of(), ahora.minusDays(8), "D5", "D6");

        // ----- Compras vigentes (para probar cancelaciones) -----
        compraPagada(repo, mateo, f1, List.of(), ahora.minusDays(1), "D6", "D7");
        compraPagada(repo, laura, f2, List.of(), ahora.minusHours(20), "D4", "D5");          // 80 %
        compraPagada(repo, laura, f3, List.of(personal), ahora.minusHours(10), "E5", "E6");  // 100 %
        compraPagada(repo, laura, f1, List.of(), ahora.minusHours(5), "C5");                 // no permitida
        compraPagada(repo, andres, f5, List.of(), ahora.minusHours(4), "A3");

        // Solicitud de recarga pendiente y tarjeta inactiva
        repo.getSolicitudes().add(new SolicitudRecarga(repo.siguienteId(), andres, 50000, ahora.minusHours(2)));
        mateo.getTarjeta().setActiva(false);

        laura.agregarNotificacion("Bienvenida a CinemaUQ. Tiene puntos próximos a vencer: úselos primero.");
        andres.agregarNotificacion("Su solicitud de recarga por $50.000 está pendiente.");
        mateo.agregarNotificacion("Su tarjeta fue bloqueada por el administrador.");
    }

    // ----- Métodos de ayuda -----

    private static Cliente cliente(RepositorioCinema repo, String nombre, String usuario) {
        Cliente cliente = new Cliente(nombre, usuario, "1234", repo.siguienteNumeroTarjeta());
        repo.getUsuarios().add(cliente);
        return cliente;
    }

    private static Pelicula pelicula(RepositorioCinema repo, String titulo, String genero, int minutos,
                                     String clasificacion) {
        Pelicula pelicula = new Pelicula(repo.siguienteId(), titulo, genero, minutos, clasificacion);
        repo.getPeliculas().add(pelicula);
        return pelicula;
    }

    private static Sala sala(RepositorioCinema repo, String nombre, int filas, int columnas) {
        Sala sala = new Sala(repo.siguienteId(), nombre, filas, columnas);
        repo.getSalas().add(sala);
        return sala;
    }

    private static Producto producto(RepositorioCinema repo, String nombre, String categoria, double precio) {
        Producto producto = new Producto(repo.siguienteId(), nombre, categoria, precio);
        repo.getConfiteria().add(producto);
        return producto;
    }

    private static Combo combo(RepositorioCinema repo, String nombre, double descuento, ItemConfiteria... items) {
        Combo combo = new Combo(repo.siguienteId(), nombre, descuento);
        for (ItemConfiteria item : items) {
            combo.agregar(item);
        }
        repo.getConfiteria().add(combo);
        return combo;
    }

    private static void promocion(RepositorioCinema repo, String nombre, TipoPromocion tipo, double valor,
                                  AlcancePromocion alcance, boolean activa) {
        Promocion promocion = new Promocion(repo.siguienteId(), nombre, tipo, valor, alcance);
        promocion.setActiva(activa);
        repo.getPromociones().add(promocion);
    }

    private static Funcion funcion(RepositorioCinema repo, Pelicula pelicula, Sala sala, LocalDateTime inicio,
                                   double precio) {
        Funcion funcion = new Funcion(repo.siguienteId(), pelicula, sala, inicio, precio);
        repo.getFunciones().add(funcion);
        return funcion;
    }

    private static void recargaHistorica(RepositorioCinema repo, Cliente cliente, double valor,
                                         LocalDateTime fecha) {
        SolicitudRecarga solicitud = new SolicitudRecarga(repo.siguienteId(), cliente, valor, fecha);
        solicitud.aprobar();
        cliente.getTarjeta().recargar(valor, "Recarga aprobada por el administrador", fecha);
        repo.getSolicitudes().add(solicitud);
    }

    /** Usa el Factory Method y el Builder igual que una compra real. */
    private static Compra compraPagada(RepositorioCinema repo, Cliente cliente, Funcion funcion,
                                       List<ItemConfiteria> items, LocalDateTime fecha, String... asientos) {
        List<Entrada> entradas = new ArrayList<>();
        for (String codigo : asientos) {
            Asiento asiento = funcion.getSala().buscarAsiento(codigo);
            entradas.add(CreadorEntrada.segunTipo(asiento.getTipo()).emitir(funcion, asiento));
        }
        Compra compra = DIRECTOR.construirCompraConTarjeta(new ConstructorCompraEstandar(), cliente,
                funcion, entradas, items, 0, fecha);
        cliente.getTarjeta().descontar(compra.getTotal(), "Compra " + compra.getCodigo(), fecha);
        for (Entrada entrada : entradas) {
            funcion.ocupar(entrada.getAsiento());
        }
        compra.pagar();
        int puntos = ReglaPuntos.puntosGanados(compra.getValorPagado());
        compra.setPuntosGanados(puntos);
        cliente.getCuentaPuntos().acumular(puntos, fecha.toLocalDate(), compra.getCodigo(),
                "Compra " + compra.getCodigo());
        repo.getCompras().add(compra);
        return compra;
    }

    private static void reembolsoHistorico(RepositorioCinema repo, Compra compra, int porcentaje, String motivo,
                                           LocalDateTime fecha) {
        double valor = Math.round(compra.getValorPagado() * porcentaje / 100.0);
        compra.cancelar();
        compra.getCliente().getTarjeta().abonarReembolso(valor,
                "Reembolso " + compra.getCodigo() + " (" + porcentaje + " %)", fecha);
        for (Entrada entrada : compra.getEntradas()) {
            compra.getFuncion().liberar(entrada.getAsiento());
        }
        compra.getCliente().getCuentaPuntos().anularPuntosDeCompra(compra.getCodigo(), fecha.toLocalDate(),
                "Anulación por reembolso " + compra.getCodigo());
        Reembolso reembolso = new Reembolso(repo.siguienteId(), compra, valor, porcentaje, motivo, fecha);
        compra.reembolsar(reembolso);
        repo.getReembolsos().add(reembolso);
    }
}
