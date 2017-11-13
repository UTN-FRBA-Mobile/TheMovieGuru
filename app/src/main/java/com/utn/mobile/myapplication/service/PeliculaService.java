package com.utn.mobile.myapplication.service;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.stream.JsonToken;
import com.utn.mobile.myapplication.MainActivity;
import com.utn.mobile.myapplication.MovieGuruApplication;
import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Genero;
import com.utn.mobile.myapplication.domain.Pelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.Date;
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

    public List<Pelicula> getRecos(boolean authentication) {

        final int user_id =  PreferenceManager.getDefaultSharedPreferences(MovieGuruApplication.getAppContext()).getInt("user-id", -1);

        int page = 1;

        String base_url;
        String url;

        if(user_id > 0)
        {
            base_url = String.format(context.getString(R.string.url_usuario), context.getString(R.string.base_url));
            String ID = String.valueOf(user_id);
            url = base_url+ ID + "/recomendaciones";
        }
        else
        {
            base_url = context.getString(R.string.base_api_reco);
            String api_key = "68325225ac8387f83699c5dddc932a8a";
            url = base_url + api_key + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page="+page+"&vote_average.gte=7";
        }
        //puede que este if tenga que ir adentro del while, VER BIEN

        String key = context.getString(R.string.cache_key_reco)+user_id+new Date();
        List<Pelicula> movies = new ArrayList<>();
        boolean remainingMovies = true;

        while(remainingMovies) {
            List<Pelicula> pagedMovies = (List<Pelicula>) get(url , key + page, authentication);
            remainingMovies = false;
            movies.addAll(pagedMovies);
            page++;
        }

        return movies;
    }

    public List<Pelicula> getAll(String search) {
        return getAll(false, search);
    }
    public List<Pelicula> getRecos() {
        return getRecos(false);
    }


    @Override
    protected List<Pelicula> deserialize(String json) {
        try {
            List<Pelicula> movies = new ArrayList<>();
            JSONArray actorJsonArray = new JSONObject(json).getJSONArray("results");

            for (int i = 0; i < actorJsonArray.length(); i++) {
                JSONObject jsonObject = actorJsonArray.getJSONObject(i);
                Pelicula peli = new Pelicula(jsonObject.getString("original_title"), jsonObject.getInt("id"));
                peli.setImg_poster(jsonObject.getString("poster_path"));
                if(jsonObject.has("release_date"))
                {
                    peli.setYear(jsonObject.getString("release_date"));
                }
                if(jsonObject.has("genre_ids"))
                {
                    JSONArray genreJsonArray = jsonObject.getJSONArray("genre_ids");
                    peli.setGeneros(getMatchingGenres(genreJsonArray));
                }
                movies.add(peli);
            }
            return movies;

        } catch (JSONException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Genero> getMatchingGenres(JSONArray jsonArray) {
        List<Genero> genres = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            int id = jsonArray.optInt(i);

            String nombre = MainActivity.getGenreById(id);

            Genero genre = new Genero(id, nombre);

            genres.add(genre);
        }
        return genres;
    }
}
