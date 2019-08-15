package info.androidhive.supegatrab.model;

public class Movie {
    private String titulo, thumbnailUrl, descipcion;
    private int valor;


    public Movie() {
    }

    public Movie(String titulo, String thumbnailUrl, int valor, String descipcion) {
        this.titulo = titulo;
        this.thumbnailUrl = thumbnailUrl;
        this.valor = valor;
        this.descipcion = descipcion;

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descipcion;
    }

    public void setDescripcion(String descipcion) {
        this.descipcion = descipcion;
    }


}