package co.edu.uniquindio.cinemauq.modelo;

/** Película de la cartelera. */
public class Pelicula {

    private final int id;
    private final String titulo;
    private final String genero;
    private final int duracionMinutos;
    private final String clasificacion;

    public Pelicula(int id, String titulo, String genero, int duracionMinutos, String clasificacion) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
