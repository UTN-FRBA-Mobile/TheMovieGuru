package com.utn.mobile.myapplication.service;


import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Actor;
import com.utn.mobile.myapplication.domain.ActorEnPelicula;
import com.utn.mobile.myapplication.domain.Imagen;
import com.utn.mobile.myapplication.domain.Pelicula;
import com.utn.mobile.myapplication.domain.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SingleActorService extends AbstractService {

    //Singleton
    private static final SingleActorService INSTANCE = new SingleActorService();
    public static SingleActorService get() {
        return INSTANCE;
    }

    private SingleActorService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public Actor getOne(int id) { return getOne(false, id); }

    public Actor getOne(boolean authentication, int id) {
        String url = String.format(context.getString(R.string.url_actor), context.getString(R.string.base_url));
        String key = context.getString(R.string.cache_key_actor)+id;
        String actorId = String.valueOf(id);
        Actor actor = (Actor) get(url + actorId, key, authentication);
        return actor;
    }

    public String addOne(Actor actor, int user_id)
    {
        String url = String.format(context.getString(R.string.url_usuario), context.getString(R.string.base_url));
        String userId = String.valueOf(user_id);
        JSONObject jsonActor = actor.toJSON();
        String response = postAuthenticated(url+userId+"/actores_favoritos",jsonActor);
        return response;
    }

    public String removeOne(int actor_id, int user_id)
    {
        String url = String.format(context.getString(R.string.url_usuario), context.getString(R.string.base_url));
        String userId = String.valueOf(user_id);
        String actorId = String.valueOf(actor_id);
        String response = remove(url+userId+"/actores_favoritos/"+actorId);
        return response;
    }

    @Override
    protected Actor deserialize(String json) {
        try {
            Actor actor = new Actor();

            JSONObject jsonObject = new JSONObject(json);
            actor.setNombre(jsonObject.getString("name"));
            actor.setId(jsonObject.getInt("id"));
            actor.setBiografia(jsonObject.getString("biography"));

            List<Pelicula> pelis = new ArrayList<>();
            List<Imagen> imagenes = new ArrayList<>();

            JSONArray moviesJsonArray = jsonObject.getJSONArray("movie_credits");
            JSONArray imagesJsonArray = jsonObject.getJSONArray("imagenes");

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonPeli = moviesJsonArray.getJSONObject(i);
                Pelicula peli = new Pelicula();
                peli.setId(jsonPeli.getInt("id"));
                peli.setNombre(jsonPeli.getString("original_title"));
                peli.setImg_poster(jsonPeli.getString("poster_path"));
                //peli.setImg_backdrop(jsonPeli.getString("backdrop_path"));
                //peli.setOverview(jsonPeli.getString("overview"));
                //peli.setTagline(jsonPeli.getString("tagline"));
                pelis.add(peli);
            }

            for (int i = 0; i < imagesJsonArray.length(); i++) {
                JSONObject jsonImg = imagesJsonArray.getJSONObject(i);
                imagenes.add(new Imagen(jsonImg.getString("file_path")));
            }

            actor.setPeliculas(pelis);
            actor.setImagenes(imagenes);

            return actor;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new Actor();
        }
    }
}
