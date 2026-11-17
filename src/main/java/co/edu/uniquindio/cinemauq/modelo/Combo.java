package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Combo formado por productos u otros combos.
 * Patrón Composite – rol: Compuesto. Su precio es la suma de sus hijos menos el descuento (RN-19).
 */
public class Combo extends ItemConfiteria {

    private final double descuentoPorcentaje;
    private final List<ItemConfiteria> items = new ArrayList<>();

    public Combo(int id, String nombre, double descuentoPorcentaje) {
        super(id, nombre);
        if (descuentoPorcentaje < 0 || descuentoPorcentaje >= 100) {
            throw new DatoInvalidoException("El descuento del combo debe estar entre 0 y 99 %.");
        }
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public void agregar(ItemConfiteria item) {
        if (item == this || item.contiene(this)) {
            throw new DatoInvalidoException("Un combo no puede contenerse a sí mismo.");
        }
        items.add(item);
    }

    public void eliminar(ItemConfiteria item) {
        items.remove(item);
    }

    /** Recorre el árbol: igual para un producto que para un combo dentro de otro combo. */
    @Override
    public double getPrecio() {
        double suma = 0;
        for (ItemConfiteria item : items) {
            suma += item.getPrecio();
        }
        return Math.round(suma * (1 - descuentoPorcentaje / 100));
    }

    @Override
    public boolean contiene(ItemConfiteria item) {
        for (ItemConfiteria hijo : items) {
            if (hijo == item || hijo.contiene(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTipo() {
        return "Combo";
    }

    @Override
    public String getDescripcion() {
        return items.stream().map(ItemConfiteria::getNombre).collect(Collectors.joining(" + "))
                + " (-" + (int) descuentoPorcentaje + " %)";
    }

    public double getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }

    public List<ItemConfiteria> getItems() {
        return items;
    }
}
