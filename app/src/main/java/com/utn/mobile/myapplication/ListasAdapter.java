package com.utn.mobile.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utn.mobile.myapplication.domain.Lista;
import com.utn.mobile.myapplication.domain.Pelicula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Master on 20-Nov-17.
 */

public class ListasAdapter extends BaseAdapter {

    protected Activity activity;
    protected List<Lista> listas;

    public ListasAdapter(Activity activity, List<Lista> listas){
        this.activity = activity;
        this.listas = listas;
    }

    public String[] getListNombreListas(){

        String[] nombres = new String[this.listas.size()];
        int index = 0;
        for (Lista l : this.listas) {
            nombres[index] = (String) l.getNombre();
            index++;
        }

        return nombres;
    }

    public boolean[] getBoolsPerteneceALista(int idPelicula){

        boolean[] booleans = new boolean[this.listas.size()];
        int index = 0;
        for (Lista l : this.listas) {
            booleans[index] = this.apareceEnLista(l,idPelicula);
            index++;
        }

        return booleans;
    }

    private boolean apareceEnLista(Lista l,int idPelicula) {

        for (Pelicula p : l.getPeliculas()) if (p.getId() == idPelicula) return true;
        return false;
    }

    @Override
    public int getCount() {
        return listas.size();
    }

    @Override
    public Object getItem(int position) {
        return listas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        Lista lista = listas.get(position);
        //TextView nombre = (TextView) v.findViewById(R.id.textView_nombre);
        return v;
    }

}
