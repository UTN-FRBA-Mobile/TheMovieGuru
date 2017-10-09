package com.utn.mobile.myapplication.service;

import com.utn.mobile.myapplication.domain.Actor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Imagen;
import com.utn.mobile.myapplication.domain.Pelicula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by lucho on 29/09/17.
 */

public class ActorService extends AbstractService {

    //Singleton
    private static final ActorService INSTANCE = new ActorService();
    public static ActorService get() {
        return INSTANCE;
    }

    private ActorService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public List<Actor> getAll(boolean authentication, String search) {
        String url = String.format(context.getString(R.string.url_actores), context.getString(R.string.base_url));
        String key = context.getString(R.string.cache_key_actor)+search;
        List<Actor> actors = new ArrayList<>();
        int page = 1;
        boolean remainingActors = true;

        while(remainingActors) {
            List<Actor> pagedActors = (List<Actor>) get(url + "&page=" + page + "&busqueda=" + search, key + page, authentication);
            remainingActors = false;
            actors.addAll(pagedActors);
            page++;
        }

        return actors;
    }

    public List<Actor> getAll(String search) {
        return getAll(false, search);
    }


    @Override
    protected List<Actor> deserialize(String json) {
        try {
            List<Actor> actors = new ArrayList<>();
            JSONArray actorJsonArray = new JSONObject(json).getJSONArray("actores");

            for (int i = 0; i < actorJsonArray.length(); i++) {
                JSONObject jsonObject = actorJsonArray.getJSONObject(i);
                actors.add(new Actor(jsonObject.getString("nombre"), jsonObject.getInt("id")));
            }
            return actors;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }


}

