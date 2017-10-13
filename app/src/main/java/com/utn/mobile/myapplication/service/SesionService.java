package com.utn.mobile.myapplication.service;

import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Usuario;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;



public class SesionService extends AbstractService {

    //Singleton
    private static final SesionService INSTANCE = new SesionService();
    public static SesionService get() {
        return INSTANCE;
    }

    private SesionService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public Usuario login(String nombreDeUsuario, String contraseña){
        Usuario u = new Usuario();
        String url = String.format(context.getString(R.string.url_login),
                                    context.getString(R.string.base_url));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", nombreDeUsuario);
            jsonObject.put("password", contraseña);


        } catch (Exception e){
            // ??????????????
        }

        u.setUsername(jsonObject.toString());
        String response = this.postAuthenticated(url, jsonObject);

        return u;
    }

    @Override
    protected Object deserialize(String json) {

        try {
            Usuario usuario = new Usuario();
            JSONObject jsonObject = new JSONObject(json);
            usuario.setUserId(jsonObject.getInt("userId"));


        }  catch (JSONException ex) {
            ex.printStackTrace();
            return new Usuario();
    }

        /*
        try {
            Actor actor = new Actor();

            JSONObject jsonObject = new JSONObject(json);
            actor.setNombre(jsonObject.getString("name"));
            actor.setId(jsonObject.getInt("id"));
            actor.setBiografia(jsonObject.getString("biography"));

            Collection<Pelicula> pelis = new ArrayList<>();
            Collection<Imagen> imagenes = new ArrayList<>();

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
         */
        return null;
    }
}
