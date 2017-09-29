package com.utn.mobile.myapplication.domain;


import java.util.ArrayList;
import java.util.Collection;

public class Lista {

    private String id;

    private String nombre;

    private Collection<Pelicula> peliculas = new ArrayList<Pelicula>();


    public Lista(String nombre)
    {
        this.nombre = nombre;
    }

    public Lista(){
    }

    public void addPelicula(Pelicula pelicula)
    {
        this.peliculas.add(pelicula);
    }
    public void removePelicula(Pelicula pelicula)
    {
        this.peliculas.remove(pelicula);
    }


	/* SETTERS & GETTERS*/

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Collection<Pelicula> getPeliculas()
    {
        return peliculas;
    }

}
