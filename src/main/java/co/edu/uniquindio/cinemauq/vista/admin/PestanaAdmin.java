package co.edu.uniquindio.cinemauq.vista.admin;

import javafx.scene.control.Tab;

/** Pestaña del panel de administración. AdminVista las trata a todas igual (polimorfismo). */
public interface PestanaAdmin {

    Tab getTab();

    void refrescar();
}
