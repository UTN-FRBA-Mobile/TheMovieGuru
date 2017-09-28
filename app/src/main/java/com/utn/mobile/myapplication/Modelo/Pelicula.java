package com.utn.mobile.myapplication.Modelo;


import java.util.ArrayList;
import java.util.Collection;

public class Pelicula {

    private int id;
    private String nombre;
    private String tagline;
    private String overview;
    private String img_poster;
    private String img_backdrop;
    protected Collection<Review> reviews =  new ArrayList<>();
    private Collection<ActorEnPelicula> cast =  new ArrayList<>();


	/* SETTERS & GETTERS*/


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTagline(){
        return tagline;
    }

    public String getOverview(){
        return overview;
    }

    public String getImg_poster(){
        return img_poster;
    }

    public String getImg_backdrop(){
        return img_backdrop;
    }

    public Collection<Review> getReviews()
    {
        return reviews;
    }

    public Collection<ActorEnPelicula> getCast()
    {
        return cast;
    }
}
