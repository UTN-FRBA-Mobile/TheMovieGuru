package com.utn.mobile.myapplication.service;


import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Pelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class PeliculaService extends AbstractService {

    //Singleton
    private static final PeliculaService INSTANCE = new PeliculaService();
    public static PeliculaService get() {
        return INSTANCE;
    }

    private PeliculaService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public List<Pelicula> getAll(boolean authentication, String search) {
        String url = String.format(context.getString(R.string.url_peliculas), context.getString(R.string.base_url));
        String key = context.getString(R.string.cache_key_pelicula)+search;
        List<Pelicula> movies = new ArrayList<>();
        int page = 1;
        boolean remainingMovies = true;

        while(remainingMovies) {
            List<Pelicula> pagedMovies = (List<Pelicula>) get(url + "&page=" + page + "&busqueda=" + search, key + page, authentication);
            remainingMovies = false;
            movies.addAll(pagedMovies);
            page++;
        }

        return movies;
    }

    public List<Pelicula> getAll(String search) {
        return getAll(false, search);
    }

    @Override
    protected List<Pelicula> deserialize(String json) {
        try {
            List<Pelicula> movies = new ArrayList<>();
            JSONArray actorJsonArray = new JSONObject(json).getJSONArray("peliculas");

            for (int i = 0; i < actorJsonArray.length(); i++) {
                JSONObject jsonObject = actorJsonArray.getJSONObject(i);
                movies.add(new Pelicula(jsonObject.getString("nombre"), jsonObject.getInt("id")));
            }
            return movies;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
