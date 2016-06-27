package com.android.pilar.listacompra;

public class Articulo {

    private String nombre;
    private boolean comprado;

    // Objeto Artículo que contiene dos variables: nombre del artículo y si ha sido o no comprado.
    public Articulo(String nombre, boolean comprado){
        this.nombre = nombre;
        this.comprado = comprado;
    }

    // Método que obtiene el nombre del artículo
    public String getNombre() {
        return nombre;
    }

    // Método que almacena el nombre del artículo
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método qe me dice si el artículo está o no comprado. Devuelve 0 (no comprado) o 1 (comprado).
    public boolean isComprado() {
        return comprado;
    }

    // Método que me asigna 0 o 1 en función de si he comprado o no el artículo
    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
