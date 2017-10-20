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
        String base_url = String.format(context.getString(R.string.api_pelicula), context.getString(R.string.base_api));
        String api_key = "68325225ac8387f83699c5dddc932a8a";
        String key = context.getString(R.string.cache_key_pelicula)+search;
        List<Pelicula> movies = new ArrayList<>();
        int page = 1;
        boolean remainingMovies = true;

        while(remainingMovies) {
            List<Pelicula> pagedMovies = (List<Pelicula>) get(base_url + api_key + "&language=en-US&query=" + search + "&page=" + page, key + page, authentication);
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
            JSONArray actorJsonArray = new JSONObject(json).getJSONArray("results");

            for (int i = 0; i < actorJsonArray.length(); i++) {
                JSONObject jsonObject = actorJsonArray.getJSONObject(i);
                Pelicula peli = new Pelicula(jsonObject.getString("title"), jsonObject.getInt("id"));
                peli.setImg_poster(jsonObject.getString("poster_path"));
                peli.setYear(jsonObject.getString("release_date"));

                movies.add(peli);
            }
            return movies;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
