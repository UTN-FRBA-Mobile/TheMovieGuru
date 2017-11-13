package com.utn.mobile.myapplication.service;

/**
 * Created by lucho on 13/11/17.
 */

import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.Imagen;
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

    @Override
    protected Pelicula deserialize(String json) {
        try {
            Pelicula pelicula = new Pelicula();

            JSONObject jsonObject = new JSONObject(json);


            return pelicula;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new Pelicula();
        }
    }
}
