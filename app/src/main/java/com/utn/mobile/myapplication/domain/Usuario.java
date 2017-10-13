package com.utn.mobile.myapplication.domain;


import java.util.ArrayList;
import java.util.Collection;

public class Usuario {

    private int userId;

    private String nombre;
    private String apellido;

    private String username;
    private String password;

    private int listasCreadas = 0;
    private Collection<Actor> actores_favoritos  = new ArrayList<>();;
    private Collection<Lista> listas_peliculas = new ArrayList<>();

    public Usuario()
    {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getListasCreadas() {
        return listasCreadas;
    }

    public void setListasCreadas(int listasCreadas) {
        this.listasCreadas = listasCreadas;
    }

    public Collection<Actor> getActores_favoritos() {
        return actores_favoritos;
    }

    public void setActores_favoritos(Collection<Actor> actores_favoritos) {
        this.actores_favoritos = actores_favoritos;
    }

    public Collection<Lista> getListas_peliculas() {
        return listas_peliculas;
    }

    public void setListas_peliculas(Collection<Lista> listas_peliculas) {
        this.listas_peliculas = listas_peliculas;
    }




}
