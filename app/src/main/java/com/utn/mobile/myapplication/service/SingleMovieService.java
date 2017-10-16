package com.utn.mobile.myapplication.service;


import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.ActorEnPelicula;
import com.utn.mobile.myapplication.domain.Pelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

public class SingleMovieService extends AbstractService {

    //Singleton
    private static final SingleMovieService INSTANCE = new SingleMovieService();
    public static SingleMovieService get() {
        return INSTANCE;
    }

    private SingleMovieService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public Pelicula getOne(int id) { return getOne(false, id); }

    public Pelicula getOne(boolean authentication, int id) {
        String url = String.format(context.getString(R.string.url_pelicula), context.getString(R.string.base_url));
        String key = context.getString(R.string.cache_key_pelicula)+id;
        String peliId = String.valueOf(id);
        Pelicula peli = (Pelicula) get(url + peliId, key, authentication);
        return peli;
    }

    @Override
    protected Pelicula deserialize(String json) {
        try {
            Pelicula peli = new Pelicula();

            JSONObject jsonObject = new JSONObject(json);

            peli.setId(jsonObject.getInt("id"));
            peli.setNombre(jsonObject.getString("original_title"));
            peli.setOverview(jsonObject.getString("overview"));
            peli.setImg_poster("poster_path");
            peli.setTagline(jsonObject.getString("tagline"));
            peli.setImg_backdrop("backdrop_path");

            Collection<ActorEnPelicula> cast = new ArrayList<>();

            JSONArray castJsonArray = jsonObject.getJSONArray("cast");

            for (int i = 0; i < castJsonArray.length(); i++) {
                JSONObject jsonPeli = castJsonArray.getJSONObject(i);
                ActorEnPelicula actor = new ActorEnPelicula();
                actor.setId(jsonPeli.getInt("id"));
                actor.setNombre(jsonPeli.getString("name"));
                actor.setCharacter(jsonPeli.getString("character"));
                cast.add(actor);
            }

            peli.setCast(cast);

            return peli;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new Pelicula();
        }
    }
}

