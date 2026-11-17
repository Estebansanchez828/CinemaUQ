package co.edu.uniquindio.cinemauq.modelo;

/** Promoción configurable por el administrador (se aplica con el patrón Decorator). */
public class Promocion {

    private final int id;
    private final String nombre;
    private final TipoPromocion tipo;
    private final double valor;
    private final AlcancePromocion alcance;
    private boolean activa = true;

    public Promocion(int id, String nombre, TipoPromocion tipo, double valor, AlcancePromocion alcance) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.alcance = alcance;
    }

    public boolean aplicaA(Vendible vendible) {
        return switch (alcance) {
            case ENTRADAS -> vendible instanceof Entrada;
            case CONFITERIA -> vendible instanceof ItemConfiteria;
            case TODO -> true;
        };
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoPromocion getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public AlcancePromocion getAlcance() {
        return alcance;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
