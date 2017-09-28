package com.utn.mobile.myapplication.Modelo;


import java.util.Collection;

public class Actor {

    private int id;
    private String nombre;
    private String biografia;
    private Collection<Imagen> imagenes;
    protected Collection<Pelicula> peliculas;


	/* SETTERS & GETTERS*/

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public Collection<Imagen> getImagenes() {
        return imagenes;
    }

    public Collection<Pelicula> getPeliculas() {
        return peliculas;
    }

}
