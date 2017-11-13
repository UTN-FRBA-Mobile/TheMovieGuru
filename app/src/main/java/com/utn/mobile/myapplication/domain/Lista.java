package com.utn.mobile.myapplication.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lista {

    private int id;

    private String nombre;

    private List<Pelicula> peliculas = new ArrayList<>();


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

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Pelicula> getPeliculas()
    {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas){
        this.peliculas = peliculas;
    }


}
