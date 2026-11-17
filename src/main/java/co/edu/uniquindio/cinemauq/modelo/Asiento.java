package co.edu.uniquindio.cinemauq.modelo;

/** Asiento de una sala (fila + número). El tipo define el precio de la entrada. */
public class Asiento {

    private final String fila;
    private final int numero;
    private TipoAsiento tipo;

    public Asiento(String fila, int numero, TipoAsiento tipo) {
        this.fila = fila;
        this.numero = numero;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return fila + numero;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public TipoAsiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAsiento tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return getCodigo();
    }
}
