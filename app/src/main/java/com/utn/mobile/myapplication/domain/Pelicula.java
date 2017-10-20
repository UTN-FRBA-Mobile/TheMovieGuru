package com.utn.mobile.myapplication.domain;


import java.util.ArrayList;
import java.util.Collection;

public class Pelicula {

    private int id;
    private String nombre;
    private String year;
    private String tagline;
    private String overview;
    private String img_poster;
    private String img_backdrop;
    protected Collection<Review> reviews =  new ArrayList<>();
    private Collection<ActorEnPelicula> cast =  new ArrayList<>();

    public Pelicula(String nombre, int id){
        this.nombre = nombre;
        this.tagline = tagline;
        this.id = id;

    }

    public Pelicula() {

    }

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

    public String getYear() { return year; }

    public void setId (int ID) { id = ID; }
    public void setNombre (String name) { nombre = name; }
    public void setTagline (String tag) { tagline = tag; }
    public void setOverview (String ov) { overview = ov; }
    public void setImg_poster (String path) { img_poster = path; }
    public void setImg_backdrop (String path) { img_backdrop = path; }
    public void setReviews (Collection<Review> revs) { reviews = revs; }
    public void setCast (Collection<ActorEnPelicula> elenco) { cast = elenco; }
    public void setYear (String anio) { year = anio; }
}
