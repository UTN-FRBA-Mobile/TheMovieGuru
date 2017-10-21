package com.utn.mobile.myapplication.domain;

public class Genero {

    private int id;
    private String nombre;

    public Genero(int ID, String name)
    {
        id = ID;
        nombre = name;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(int ID) { id = ID; }
    public void setNombre(String name) { nombre = name; }

}
