package com.utn.mobile.myapplication.service;


import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Genero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class GenreService extends AbstractService {



    //Singleton
    private static final GenreService INSTANCE = new GenreService();
    public static GenreService get() {
        return INSTANCE;
    }

    private GenreService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public List<Genero> getAll(boolean authentication) {
        String base_url = "https://api.themoviedb.org/3/genre/movie/list?api_key=";
        String api_key = "68325225ac8387f83699c5dddc932a8a";
        String key = context.getString(R.string.cache_key_pelicula);
        List<Genero> genres = (List<Genero>) get(base_url + api_key + "&language=en-US", key, authentication);

        return genres;
    }

    public List<Genero> getAll() {
        return getAll(false);
    }


    @Override
    protected List<Genero> deserialize(String json) {
        try {
            List<Genero> genres = new ArrayList<>();
            JSONArray genreJsonArray = new JSONObject(json).getJSONArray("genres");

            for (int i = 0; i < genreJsonArray.length(); i++) {
                JSONObject jsonObject = genreJsonArray.getJSONObject(i);

                Genero genre = new Genero(jsonObject.getInt("id"), jsonObject.getString("name"));

                genres.add(genre);
            }
            return genres;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

}
