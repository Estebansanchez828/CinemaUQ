package co.edu.uniquindio.cinemauq.servicio;

import co.edu.uniquindio.cinemauq.datos.RepositorioCinema;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Compra;
import co.edu.uniquindio.cinemauq.modelo.Entrada;
import co.edu.uniquindio.cinemauq.modelo.EstadoFuncion;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.ItemConfiteria;
import co.edu.uniquindio.cinemauq.modelo.Movimiento;
import co.edu.uniquindio.cinemauq.modelo.Reembolso;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.TipoMovimiento;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/** Calcula las estadísticas a partir del repositorio (incluye los datos históricos). */
public class ServicioEstadisticasImpl implements ServicioEstadisticas {

    private static final DateTimeFormatter MES = DateTimeFormatter.ofPattern("yyyy-MM");

    private final RepositorioCinema repositorio;

    public ServicioEstadisticasImpl(RepositorioCinema repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public int ventasRealizadas() {
        return (int) repositorio.getCompras().stream().filter(Compra::fueVendida).count();
    }

    @Override
    public double ingresosNetos() {
        double ventas = repositorio.getCompras().stream().filter(Compra::fueVendida)
                .mapToDouble(Compra::getValorPagado).sum();
        return ventas - valorReembolsado();
    }

    @Override
    public int reembolsosRealizados() {
        return repositorio.getReembolsos().size();
    }

    @Override
    public double valorReembolsado() {
        return repositorio.getReembolsos().stream().mapToDouble(Reembolso::getValor).sum();
    }

    /** Asientos vendidos / capacidad de todas las funciones no canceladas de cada sala. */
    @Override
    public Map<String, Double> ocupacionPorSala() {
        Map<String, Double> resultado = new LinkedHashMap<>();
        for (Sala sala : repositorio.getSalas()) {
            int ocupados = 0;
            int capacidad = 0;
            for (Funcion funcion : repositorio.getFunciones()) {
                if (funcion.getSala() == sala && funcion.getEstado() != EstadoFuncion.CANCELADA) {
                    ocupados += funcion.getAsientosOcupados().size();
                    capacidad += sala.getCapacidad();
                }
            }
            resultado.put(sala.getNombre(), capacidad == 0 ? 0 : ocupados * 100.0 / capacidad);
        }
        return resultado;
    }

    @Override
    public Map<String, Integer> entradasPorPelicula() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        repositorio.getPeliculas().forEach(p -> conteo.put(p.getTitulo(), 0));
        for (Compra compra : repositorio.getCompras()) {
            if (compra.estaPagada()) {
                for (Entrada entrada : compra.getEntradas()) {
                    conteo.merge(entrada.getFuncion().getPelicula().getTitulo(), 1, Integer::sum);
                }
            }
        }
        return ordenarDesc(conteo);
    }

    @Override
    public Map<String, Integer> productosMasVendidos() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Compra compra : repositorio.getCompras()) {
            if (compra.estaPagada()) {
                for (ItemConfiteria item : compra.getItems()) {
                    conteo.merge(item.getNombre(), 1, Integer::sum);
                }
            }
        }
        return ordenarDesc(conteo);
    }

    @Override
    public Map<String, Integer> entradasPorSala() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        repositorio.getSalas().forEach(s -> conteo.put(s.getNombre(), 0));
        for (Compra compra : repositorio.getCompras()) {
            if (compra.estaPagada()) {
                for (Entrada entrada : compra.getEntradas()) {
                    conteo.merge(entrada.getFuncion().getSala().getNombre(), 1, Integer::sum);
                }
            }
        }
        return conteo;
    }

    @Override
    public Map<String, Double> ventasPorCategoria() {
        double entradas = 0;
        double confiteria = 0;
        for (Compra compra : repositorio.getCompras()) {
            if (compra.estaPagada()) {
                entradas += compra.getEntradas().stream().mapToDouble(Entrada::getPrecio).sum();
                confiteria += compra.getItems().stream().mapToDouble(ItemConfiteria::getPrecio).sum();
            }
        }
        Map<String, Double> resultado = new LinkedHashMap<>();
        resultado.put("Entradas", entradas);
        resultado.put("Confitería", confiteria);
        return resultado;
    }

    @Override
    public Map<String, Double> ventasPorMes() {
        Map<String, Double> resultado = new TreeMap<>();
        for (Compra compra : repositorio.getCompras()) {
            if (compra.fueVendida()) {
                resultado.merge(compra.getFecha().format(MES), compra.getValorPagado(), Double::sum);
            }
        }
        return resultado;
    }

    @Override
    public Map<String, Double> recargasPorMes() {
        Map<String, Double> resultado = new TreeMap<>();
        for (Cliente cliente : repositorio.getClientes()) {
            for (Movimiento movimiento : cliente.getTarjeta().getMovimientos()) {
                if (movimiento.getTipo() == TipoMovimiento.RECARGA) {
                    resultado.merge(movimiento.getFecha().format(MES), movimiento.getValor(), Double::sum);
                }
            }
        }
        return resultado;
    }

    private Map<String, Integer> ordenarDesc(Map<String, Integer> mapa) {
        Map<String, Integer> ordenado = new LinkedHashMap<>();
        mapa.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> ordenado.put(e.getKey(), e.getValue()));
        return ordenado;
    }
}
