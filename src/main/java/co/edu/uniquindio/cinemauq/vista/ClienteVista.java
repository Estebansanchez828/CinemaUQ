package co.edu.uniquindio.cinemauq.vista;

import co.edu.uniquindio.cinemauq.App;
import co.edu.uniquindio.cinemauq.controlador.ClienteControlador;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.LotePuntos;
import co.edu.uniquindio.cinemauq.modelo.MetodoPago;
import co.edu.uniquindio.cinemauq.modelo.Movimiento;
import co.edu.uniquindio.cinemauq.modelo.MovimientoPuntos;
import co.edu.uniquindio.cinemauq.modelo.Notificacion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.ReglaPuntos;
import co.edu.uniquindio.cinemauq.modelo.SolicitudRecarga;
import co.edu.uniquindio.cinemauq.modelo.TipoAsiento;
import co.edu.uniquindio.cinemauq.servicio.fachada.Cotizacion;
import co.edu.uniquindio.cinemauq.util.Formato;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Vista del cliente (RF-003 a RF-014): cartelera y compra con selección gráfica de asientos,
 * tarjeta virtual, compras, puntos y notificaciones.
 */
public class ClienteVista {

    private static final String COLOR_DISPONIBLE = "#43a047";
    private static final String COLOR_PREFERENCIAL = "#fb8c00";
    private static final String COLOR_ACCESIBLE = "#8e24aa";
    private static final String COLOR_SELECCIONADO = "#1e88e5";
    private static final String COLOR_OCUPADO = "#e53935";

    private final ClienteControlador controlador;
    private final BorderPane raiz = new BorderPane();
    private final Label lblEncabezado = new Label();

    // Compra
    private final ListView<Pelicula> listaPeliculas = new ListView<>();
    private final Label lblPelicula = new Label("Seleccione una película");
    private final ListView<Funcion> listaFunciones = new ListView<>();
    private final GridPane gridAsientos = new GridPane();
    private final Label lblFuncion = new Label("Seleccione una función para ver los asientos");
    private final ListView<ItemConfiteria> listaConfiteria = new ListView<>();
    private final ListView<ItemConfiteria> listaCarrito = new ListView<>();
    private final TextArea txtResumen = new TextArea();
    private final Set<Asiento> seleccionados = new LinkedHashSet<>();
    private Funcion funcionSeleccionada;

    // Tarjeta
    private final Label lblTarjeta = new Label();
    private final TableView<Movimiento> tablaMovimientos = Tablas.crear();
    private final TableView<SolicitudRecarga> tablaSolicitudes = Tablas.crear();

    // Compras
    private final TableView<Compra> tablaCompras = Tablas.crear();

    // Puntos
    private final Label lblPuntos = new Label();
    private final TableView<LotePuntos> tablaLotes = Tablas.crear();
    private final TableView<MovimientoPuntos> tablaMovPuntos = Tablas.crear();

    // Notificaciones
    private final ListView<String> listaNotificaciones = new ListView<>();

    public ClienteVista(Cliente cliente) {
        this.controlador = new ClienteControlador(App.getSistema(), cliente);

        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.setOnAction(e -> App.mostrar(new LoginVista().getRaiz(), "CinemaUQ - Iniciar sesión"));
        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);
        lblEncabezado.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        HBox barra = new HBox(10, lblEncabezado, espacio, btnSalir);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(10));
        barra.setStyle("-fx-background-color: #eceff1;");

        TabPane pestanas = new TabPane();
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        pestanas.getTabs().add(new Tab("Cartelera y compra", crearPestanaCompra()));
        pestanas.getTabs().add(new Tab("Mi tarjeta", crearPestanaTarjeta()));
        pestanas.getTabs().add(new Tab("Mis compras", crearPestanaCompras()));
        pestanas.getTabs().add(new Tab("Mis puntos", crearPestanaPuntos()));
        pestanas.getTabs().add(new Tab("Notificaciones", listaNotificaciones));

        raiz.setTop(barra);
        raiz.setCenter(pestanas);
        refrescar();
    }

    // =====================================================================
    // Pestaña: cartelera y compra
    // =====================================================================

    private Parent crearPestanaCompra() {
        // Izquierda: películas y funciones
        listaPeliculas.setPrefHeight(200);
        listaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nueva) -> {
            if (nueva != null) {
                lblPelicula.setText(nueva.getGenero() + " | " + nueva.getDuracionMinutos() + " min | "
                        + nueva.getClasificacion());
                listaFunciones.setItems(FXCollections.observableArrayList(controlador.funcionesDisponibles(nueva)));
            }
        });
        listaFunciones.getSelectionModel().selectedItemProperty().addListener((obs, vieja, nueva) -> {
            funcionSeleccionada = nueva;
            seleccionados.clear();
            dibujarAsientos();
            actualizarResumen();
        });
        lblPelicula.setWrapText(true);
        VBox izquierda = new VBox(8, titulo("Cartelera"), listaPeliculas, lblPelicula,
                titulo("Funciones"), listaFunciones);
        izquierda.setPrefWidth(270);
        VBox.setVgrow(listaFunciones, Priority.ALWAYS);

        // Centro: cuadrícula de asientos
        gridAsientos.setHgap(4);
        gridAsientos.setVgap(4);
        gridAsientos.setAlignment(Pos.CENTER);
        Label pantalla = new Label("PANTALLA");
        pantalla.setMaxWidth(Double.MAX_VALUE);
        pantalla.setAlignment(Pos.CENTER);
        pantalla.setStyle("-fx-background-color: #37474f; -fx-text-fill: white; -fx-padding: 4;");
        HBox leyenda = new HBox(8,
                muestra("Disponible", COLOR_DISPONIBLE), muestra("Preferencial", COLOR_PREFERENCIAL),
                muestra("Accesible", COLOR_ACCESIBLE), muestra("Seleccionado", COLOR_SELECCIONADO),
                muestra("Ocupado", COLOR_OCUPADO));
        leyenda.setAlignment(Pos.CENTER);
        lblFuncion.setWrapText(true);
        VBox centro = new VBox(10, titulo("Selección de asientos"), lblFuncion, pantalla, gridAsientos, leyenda);
        centro.setPadding(new Insets(0, 10, 0, 10));
        ScrollPane scrollCentro = new ScrollPane(centro);
        scrollCentro.setFitToWidth(true);

        // Derecha: confitería, carrito, resumen y pago
        listaConfiteria.setItems(FXCollections.observableArrayList(controlador.confiteria()));
        listaConfiteria.setPrefHeight(170);
        listaConfiteria.setCellFactory(lista -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(ItemConfiteria item, boolean vacio) {
                super.updateItem(item, vacio);
                setText(vacio || item == null ? null
                        : item.getNombre() + " (" + item.getTipo() + ") - " + Formato.dinero(item.getPrecio()));
            }
        });
        Button btnAgregar = new Button("Agregar ↓");
        btnAgregar.setOnAction(e -> {
            ItemConfiteria item = listaConfiteria.getSelectionModel().getSelectedItem();
            if (item != null) {
                listaCarrito.getItems().add(item);
                actualizarResumen();
            }
        });
        Button btnQuitar = new Button("Quitar");
        btnQuitar.setOnAction(e -> {
            int indice = listaCarrito.getSelectionModel().getSelectedIndex();
            if (indice >= 0) {
                listaCarrito.getItems().remove(indice);
                actualizarResumen();
            }
        });
        listaCarrito.setPrefHeight(110);
        txtResumen.setEditable(false);
        txtResumen.setWrapText(true);
        txtResumen.setPrefRowCount(9);
        Button btnTarjeta = new Button("Pagar con tarjeta");
        btnTarjeta.setMaxWidth(Double.MAX_VALUE);
        btnTarjeta.setStyle("-fx-base: #43a047; -fx-font-weight: bold;");
        btnTarjeta.setOnAction(e -> pagar(MetodoPago.TARJETA));
        Button btnPuntos = new Button("Redimir con puntos");
        btnPuntos.setMaxWidth(Double.MAX_VALUE);
        btnPuntos.setOnAction(e -> pagar(MetodoPago.PUNTOS));
        Button btnLimpiar = new Button("Limpiar selección");
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setOnAction(e -> limpiarCompra());
        VBox derecha = new VBox(8, titulo("Confitería y combos"), listaConfiteria, new HBox(8, btnAgregar, btnQuitar),
                titulo("Mi pedido de confitería"), listaCarrito, titulo("Resumen"), txtResumen,
                btnTarjeta, btnPuntos, btnLimpiar);
        derecha.setPrefWidth(340);

        BorderPane panel = new BorderPane();
        panel.setLeft(izquierda);
        panel.setCenter(scrollCentro);
        panel.setRight(derecha);
        panel.setPadding(new Insets(10));
        return panel;
    }

    /** Dibuja la cuadrícula de asientos con colores según disponibilidad y tipo (RF-004). */
    private void dibujarAsientos() {
        gridAsientos.getChildren().clear();
        if (funcionSeleccionada == null) {
            lblFuncion.setText("Seleccione una función para ver los asientos");
            return;
        }
        Funcion funcion = funcionSeleccionada;
        lblFuncion.setText(funcion + " | Precio base " + Formato.dinero(funcion.getPrecioBase())
                + " | Preferencial +25 % | Accesible -20 % | Ocupación " + String.format("%.0f", funcion.getOcupacion()) + " %");
        for (Asiento asiento : funcion.getSala().getAsientos()) {
            Button boton = new Button(asiento.getCodigo());
            boton.setPrefSize(52, 34);
            pintar(boton, asiento);
            boton.setOnAction(e -> {
                if (funcion.estaOcupado(asiento)) {
                    Alertas.error("El asiento " + asiento + " ya está ocupado.");
                    return;
                }
                if (!seleccionados.remove(asiento)) {
                    seleccionados.add(asiento);
                }
                pintar(boton, asiento);
                actualizarResumen();
            });
            int fila = asiento.getFila().charAt(0) - 'A';
            gridAsientos.add(boton, asiento.getNumero() - 1, fila);
        }
    }

    private void pintar(Button boton, Asiento asiento) {
        String color;
        if (funcionSeleccionada.estaOcupado(asiento)) {
            color = COLOR_OCUPADO;
        } else if (seleccionados.contains(asiento)) {
            color = COLOR_SELECCIONADO;
        } else if (asiento.getTipo() == TipoAsiento.ACCESIBLE) {
            color = COLOR_ACCESIBLE;
        } else if (asiento.getTipo() == TipoAsiento.PREFERENCIAL) {
            color = COLOR_PREFERENCIAL;
        } else {
            color = COLOR_DISPONIBLE;
        }
        boton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    private void actualizarResumen() {
        List<Asiento> asientos = new ArrayList<>(seleccionados);
        List<ItemConfiteria> items = new ArrayList<>(listaCarrito.getItems());
        if (asientos.isEmpty() && items.isEmpty()) {
            txtResumen.setText("Seleccione asientos y/o productos.");
            return;
        }
        try {
            Cotizacion cotizacion = controlador.cotizar(funcionSeleccionada, asientos, items);
            StringBuilder texto = new StringBuilder();
            cotizacion.getDetalle().forEach(linea -> texto.append("• ").append(linea).append("\n"));
            texto.append("\nSubtotal: ").append(Formato.dinero(cotizacion.getSubtotal()))
                    .append("\nPromociones: -").append(Formato.dinero(cotizacion.getDescuento()))
                    .append("\nTOTAL CON TARJETA: ").append(Formato.dinero(cotizacion.getTotal()))
                    .append("\nO REDIMIR CON: ").append(cotizacion.getPuntosNecesarios()).append(" puntos (sin promociones)");
            txtResumen.setText(texto.toString());
        } catch (CinemaException e) {
            txtResumen.setText(e.getMessage());
        }
    }

    private void pagar(MetodoPago metodo) {
        List<Asiento> asientos = new ArrayList<>(seleccionados);
        List<ItemConfiteria> items = new ArrayList<>(listaCarrito.getItems());
        try {
            Cotizacion cotizacion = controlador.cotizar(funcionSeleccionada, asientos, items);
            String pregunta = metodo == MetodoPago.TARJETA
                    ? "¿Pagar " + Formato.dinero(cotizacion.getTotal()) + " con la tarjeta "
                      + controlador.getCliente().getTarjeta().getNumero() + "?"
                    : "¿Redimir " + cotizacion.getPuntosNecesarios() + " puntos?";
            if (!Alertas.confirmar(pregunta)) {
                return;
            }
            Compra compra = controlador.comprar(funcionSeleccionada, asientos, items, metodo);
            Alertas.info("Compra " + compra.getCodigo() + " aprobada (" + compra.getNombreEstado() + ").\n"
                    + (metodo == MetodoPago.TARJETA
                    ? "Valor: " + Formato.dinero(compra.getTotal()) + ". Puntos ganados: " + compra.getPuntosGanados()
                    : "Puntos usados: " + compra.getPuntosUsados()));
            limpiarCompra();
            refrescar();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void limpiarCompra() {
        seleccionados.clear();
        listaCarrito.getItems().clear();
        dibujarAsientos();
        actualizarResumen();
    }

    // =====================================================================
    // Pestaña: tarjeta virtual
    // =====================================================================

    private Parent crearPestanaTarjeta() {
        lblTarjeta.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Tablas.columna(tablaMovimientos, "Fecha", m -> Formato.fechaHora(m.getFecha()));
        Tablas.columna(tablaMovimientos, "Tipo", m -> m.getTipo().toString());
        Tablas.columna(tablaMovimientos, "Valor", m -> Formato.dinero(m.getValor()));
        Tablas.columna(tablaMovimientos, "Saldo resultante", m -> Formato.dinero(m.getSaldoResultante()));
        Tablas.columna(tablaMovimientos, "Descripción", Movimiento::getDescripcion);

        TextField txtValor = new TextField();
        txtValor.setPromptText("Valor, ej: 50000");
        Button btnSolicitar = new Button("Solicitar recarga");
        btnSolicitar.setOnAction(e -> {
            try {
                SolicitudRecarga solicitud = controlador.solicitarRecarga(txtValor.getText());
                Alertas.info("Solicitud " + solicitud.getId() + " por " + Formato.dinero(solicitud.getValor())
                        + " enviada. El administrador debe aprobarla.");
                txtValor.clear();
                refrescar();
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        Tablas.columna(tablaSolicitudes, "Fecha", s -> Formato.fechaHora(s.getFecha()));
        Tablas.columna(tablaSolicitudes, "Valor", s -> Formato.dinero(s.getValor()));
        Tablas.columna(tablaSolicitudes, "Estado", s -> s.getEstado().toString());
        tablaSolicitudes.setPrefHeight(160);

        VBox panel = new VBox(10, lblTarjeta, titulo("Movimientos"), tablaMovimientos,
                titulo("Solicitar recarga (solo el administrador recarga)"), new HBox(8, txtValor, btnSolicitar),
                tablaSolicitudes);
        VBox.setVgrow(tablaMovimientos, Priority.ALWAYS);
        panel.setPadding(new Insets(10));
        return panel;
    }

    // =====================================================================
    // Pestaña: mis compras
    // =====================================================================

    private Parent crearPestanaCompras() {
        Tablas.columna(tablaCompras, "Código", Compra::getCodigo);
        Tablas.columna(tablaCompras, "Fecha", c -> Formato.fechaHora(c.getFecha()));
        Tablas.columna(tablaCompras, "Función", c -> c.getFuncion() == null ? "Solo confitería" : c.getFuncion().toString());
        Tablas.columna(tablaCompras, "Detalle", Compra::getResumen);
        Tablas.columna(tablaCompras, "Total", c -> Formato.dinero(c.getTotal()));
        Tablas.columna(tablaCompras, "Pago", c -> c.getMetodoPago() == MetodoPago.TARJETA
                ? "Tarjeta" : c.getPuntosUsados() + " puntos");
        Tablas.columna(tablaCompras, "Puntos ganados", c -> String.valueOf(c.getPuntosGanados()));
        Tablas.columna(tablaCompras, "Estado", Compra::getNombreEstado);

        Button btnCancelar = new Button("Cancelar compra seleccionada");
        btnCancelar.setOnAction(e -> {
            Compra compra = tablaCompras.getSelectionModel().getSelectedItem();
            if (compra == null) {
                Alertas.error("Seleccione una compra.");
                return;
            }
            if (!Alertas.confirmar("¿Cancelar la compra " + compra.getCodigo() + "?")) {
                return;
            }
            try {
                Reembolso reembolso = controlador.cancelar(compra);
                Alertas.info("Compra cancelada. Reembolso del " + reembolso.getPorcentaje() + " %: "
                        + Formato.dinero(reembolso.getValor()) + " (" + reembolso.getMotivo() + ").");
                refrescar();
            } catch (CinemaException ex) {
                Alertas.error(ex.getMessage());
            }
        });
        Label reglas = new Label("Cancelación: más de 24 h = 100 % · entre 2 y 24 h = 80 % · menos de 2 h = no se permite. "
                + "Las compras con puntos y las de solo confitería no se cancelan.");
        reglas.setWrapText(true);
        VBox panel = new VBox(10, titulo("Historial de compras"), tablaCompras, reglas, btnCancelar);
        VBox.setVgrow(tablaCompras, Priority.ALWAYS);
        panel.setPadding(new Insets(10));
        return panel;
    }

    // =====================================================================
    // Pestaña: mis puntos
    // =====================================================================

    private Parent crearPestanaPuntos() {
        lblPuntos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label regla = new Label("Regla: 1 punto por cada $" + ReglaPuntos.PESOS_POR_PUNTO_GANADO
                + " pagados con tarjeta. Redimir cuesta el precio ÷ " + ReglaPuntos.PESOS_POR_PUNTO_REDIMIDO
                + " (entrada general de $12.000 = 1.200 puntos; Combo pareja de $24.000 = 2.400 puntos). "
                + "Los puntos vencen al año y se usan primero los que vencen antes.");
        regla.setWrapText(true);
        LocalDate hoy = LocalDate.now();
        Tablas.columna(tablaLotes, "Puntos", l -> String.valueOf(l.getPuntos()));
        Tablas.columna(tablaLotes, "Disponibles", l -> String.valueOf(l.getDisponibles()));
        Tablas.columna(tablaLotes, "Obtención", l -> Formato.fecha(l.getFechaObtencion()));
        Tablas.columna(tablaLotes, "Vencimiento", l -> Formato.fecha(l.getFechaVencimiento()));
        Tablas.columna(tablaLotes, "Estado", l -> l.getEstado(hoy));
        Tablas.columna(tablaLotes, "Compra", LotePuntos::getCodigoCompra);
        Tablas.columna(tablaMovPuntos, "Fecha", m -> Formato.fecha(m.getFecha()));
        Tablas.columna(tablaMovPuntos, "Tipo", m -> m.getTipo().toString());
        Tablas.columna(tablaMovPuntos, "Cantidad", m -> String.valueOf(m.getCantidad()));
        Tablas.columna(tablaMovPuntos, "Descripción", MovimientoPuntos::getDescripcion);
        VBox panel = new VBox(10, lblPuntos, regla, titulo("Lotes (ordenados por vencimiento)"), tablaLotes,
                titulo("Movimientos de puntos"), tablaMovPuntos);
        VBox.setVgrow(tablaLotes, Priority.ALWAYS);
        panel.setPadding(new Insets(10));
        return panel;
    }

    // =====================================================================
    // Comunes
    // =====================================================================

    private void refrescar() {
        Cliente cliente = controlador.getCliente();
        int puntos = controlador.saldoPuntos();
        lblEncabezado.setText("Hola, " + cliente.getNombre() + "   |   Saldo: " + Formato.dinero(cliente.getTarjeta().getSaldo())
                + "   |   Puntos vigentes: " + puntos);
        Pelicula peliculaActual = listaPeliculas.getSelectionModel().getSelectedItem();
        Funcion funcionActual = funcionSeleccionada;
        listaPeliculas.setItems(FXCollections.observableArrayList(controlador.cartelera()));
        if (peliculaActual != null) {
            listaPeliculas.getSelectionModel().select(peliculaActual);
            if (funcionActual != null && listaFunciones.getItems().contains(funcionActual)) {
                listaFunciones.getSelectionModel().select(funcionActual);
            }
        }
        lblTarjeta.setText("Tarjeta " + cliente.getTarjeta().getNumero() + " | "
                + (cliente.getTarjeta().isActiva() ? "ACTIVA" : "INACTIVA") + " | Saldo: "
                + Formato.dinero(cliente.getTarjeta().getSaldo()));
        Tablas.llenar(tablaMovimientos, controlador.movimientos());
        Tablas.llenar(tablaSolicitudes, controlador.misSolicitudes());
        Tablas.llenar(tablaCompras, controlador.misCompras());
        lblPuntos.setText("Puntos vigentes: " + puntos);
        Tablas.llenar(tablaLotes, controlador.lotes());
        Tablas.llenar(tablaMovPuntos, controlador.movimientosPuntos());
        listaNotificaciones.setItems(FXCollections.observableArrayList(
                controlador.notificaciones().stream().map(Notificacion::toString).toList()));
    }

    private Label titulo(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        return label;
    }

    private Label muestra(String texto, String color) {
        Label label = new Label(texto);
        label.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 3 8 3 8;");
        return label;
    }

    public Parent getRaiz() {
        return raiz;
    }
}
