package com.utn.mobile.myapplication.domain;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Actor {

    private int id;
    private String nombre;
    private String biografia;
    private List<Imagen> imagenes = new ArrayList<>();
    protected List<Pelicula> peliculas = new ArrayList<>();

    public Actor(String nombre, int id){
        this.nombre = nombre;
        this.biografia = biografia;
        this.id = id;

    }

    public Actor() {

    }

    public JSONObject toJSON()
    {
        try {
            JSONObject json = new JSONObject();

            json.put("id", this.getId());
            json.put("name", this.getNombre());
            json.put("biography", this.getBiografia());

            List<Imagen> imagenes = this.getImagenes();
            JSONArray imagenesJsonArray = new JSONArray();

            for(int i=0; i<imagenes.size(); i++)
            {
                Imagen img = imagenes.get(i);
                JSONObject imagenJSON = new JSONObject();
                imagenJSON.put("file_path", img.getUrl());
                imagenesJsonArray.put(imagenJSON);
            }

            List<Pelicula> pelis = this.getPeliculas();
            JSONArray pelisJsonArray = new JSONArray();
            for(int i=0; i<pelis.size(); i++)
            {
                Pelicula peli = pelis.get(i);
                JSONObject peliJSON = new JSONObject();
                peliJSON.put("id", peli.getId());
                peliJSON.put("cast", new JSONArray());
                peliJSON.put("original_title", peli.getNombre());
                peliJSON.put("tagline", null);
                peliJSON.put("overview", peli.getOverview());
                peliJSON.put("poster_path", peli.getImg_poster());
                peliJSON.put("backdrop_path", peli.getImg_backdrop());
                peliJSON.put("reviews", new JSONArray());
                pelisJsonArray.put(peliJSON);
            }

            json.put("imagenes", imagenesJsonArray);
            json.put("movie_credits", pelisJsonArray);

            return json;
        }
        catch (JSONException je)
        {
            return new JSONObject();
        }
    }


    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Actor))return false;
        Actor oActor = (Actor)o;
        return (oActor.getId() == this.id);
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

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setId(int ID) { id = ID; }
    public void setNombre(String name) { nombre = name; }
    public void setBiografia(String bio) { biografia = bio; }
    public void setImagenes(List<Imagen> images) { imagenes = images; }
    public void setPeliculas(List<Pelicula> movies) { peliculas = movies; }

    public void addImagen(Imagen imagen) { imagenes.add(imagen); }

}
