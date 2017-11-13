package com.utn.mobile.myapplication.service;

/**
 * Created by lucho on 13/11/17.
 */

import com.google.gson.JsonArray;
import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Imagen;
import com.utn.mobile.myapplication.domain.Lista;
import com.utn.mobile.myapplication.domain.Pelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class ListService extends AbstractService {
    //Singleton
    private static final ListService INSTANCE = new ListService();
    public static ListService get() {
        return INSTANCE;
    }

    private ListService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public String addOne(Pelicula pelicula)
    {
        String url = String.format(context.getString(R.string.url_listas), context.getString(R.string.base_url));
        String nombrePelicula = pelicula.getNombre();
        JSONObject jsonPelicula = new JSONObject();
        String response;
        try {
            jsonPelicula.put("nombre", nombrePelicula);
            response = postAuthenticated(url,jsonPelicula);
        } catch (JSONException ex) {
            ex.printStackTrace();
            response = null;
        }
        return response;
    }

    public List<Lista> getAll() {
        String base_url = String.format(context.getString(R.string.url_listas), context.getString(R.string.base_url));
        String key = "listas_user"; //Cambiar para que no devuelva siempre lo mismo
        List<Lista> lists = new ArrayList<>();
        int page = 1;
        boolean remainingListas = true;

        while(remainingListas) {
            List<Lista> pagedLists = (List<Lista>) get(base_url, key + page, true, true);
            remainingListas = false;
            lists.addAll(pagedLists);
            page++;
        }

        return lists;
    }

    @Override
    protected List<Lista> deserialize(String json) {
        try {
            List<Lista> listas = new ArrayList<>();
            JSONArray listJsonArray = new JSONArray(json);

            for (int i = 0; i < listJsonArray.length(); i++) {
                JSONObject jsonObject = listJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                Lista lista = new Lista(nombre);
                JSONArray moviesJsonArray = jsonObject.getJSONArray("peliculas");
                List<Pelicula> pelis = new ArrayList<>();
                for (int j = 0; j < moviesJsonArray.length(); j++) {
                    JSONObject jsonPeli = moviesJsonArray.getJSONObject(j);
                    Pelicula peli = new Pelicula();
                    peli.setId(jsonPeli.getInt("id"));
                    peli.setNombre(jsonPeli.getString("original_title"));
                    peli.setImg_poster(jsonPeli.getString("poster_path"));
                    pelis.add(peli);
                }
                lista.setPeliculas(pelis);
                listas.add(lista);
            }

            return listas;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
