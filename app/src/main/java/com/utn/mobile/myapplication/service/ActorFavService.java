package com.utn.mobile.myapplication.service;


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

public class ActorFavService extends AbstractService {
    //Singleton
    private static final ActorFavService INSTANCE = new ActorFavService();
    public static ActorFavService get() {
        return INSTANCE;
    }

    private ActorFavService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public List<Actor> getAll(boolean authentication, int user_id) {
        String base_url = String.format(context.getString(R.string.url_usuario), context.getString(R.string.base_url));
        String userId = String.valueOf(user_id);
        String key = context.getString(R.string.cache_key_actor)+userId;

        List<Actor> actors = (List<Actor>) get(base_url + userId + "/actores_favoritos", key, authentication);

        return actors;
    }

    public List<Actor> getAll(int user_id) {
        return getAll(false, user_id);
    }


    @Override
    protected List<Actor> deserialize(String json) {
        try {
            List<Actor> actors = new ArrayList<>();
            JSONArray actorJsonArray = new JSONArray(json);

            for (int i = 0; i < actorJsonArray.length(); i++) {
                JSONObject jsonObject = actorJsonArray.getJSONObject(i);

                Actor actor = new Actor();
                actor.setNombre(jsonObject.getString("name"));
                actor.setId(jsonObject.getInt("id"));
                actor.setBiografia(jsonObject.getString("biography"));

                List<Pelicula> pelis = new ArrayList<>();
                List<Imagen> imagenes = new ArrayList<>();

                JSONArray moviesJsonArray = jsonObject.getJSONArray("movie_credits");
                JSONArray imagesJsonArray = jsonObject.getJSONArray("imagenes");

                for (int j = 0; j < moviesJsonArray.length(); j++) {
                    JSONObject jsonPeli = moviesJsonArray.getJSONObject(i);
                    Pelicula peli = new Pelicula();
                    peli.setId(jsonPeli.getInt("id"));
                    peli.setNombre(jsonPeli.getString("original_title"));
                    peli.setImg_poster(jsonPeli.getString("poster_path"));
                    pelis.add(peli);
                }

                for (int j = 0; j < imagesJsonArray.length(); j++) {
                    JSONObject jsonImg = imagesJsonArray.getJSONObject(i);
                    imagenes.add(new Imagen(jsonImg.getString("file_path")));
                }

                actor.setPeliculas(pelis);
                actor.setImagenes(imagenes);

                actors.add(actor);
            }
            return actors;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
