package co.edu.uniquindio.cinemauq;

/**
 * Punto de entrada para ejecutar con el botón Run de IntelliJ.
 * Llama a App (que extiende Application) para evitar el error
 * "JavaFX runtime components are missing" sin usar module-info.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
