package com.utn.mobile.myapplication.domain;


import java.util.ArrayList;
import java.util.Collection;

public class Actor {

    private int id;
    private String nombre;
    private String biografia;
    private Collection<Imagen> imagenes = new ArrayList<>();
    protected Collection<Pelicula> peliculas = new ArrayList<>();

    public Actor(String nombre, int id){
        this.nombre = nombre;
        this.biografia = biografia;
        this.id = id;

    }
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
