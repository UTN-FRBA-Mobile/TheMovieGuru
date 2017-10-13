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


        String response = this.postAuthenticated(url, jsonObject);
        u = (Usuario) deserialize(response);
        return u;
    }

    @Override
    protected Object deserialize(String json) {

        try {
            Usuario usuario = new Usuario();
            JSONObject jsonObject = new JSONObject(json);
            usuario.setUserId(jsonObject.getInt("userId"));
            return usuario;

        }  catch (JSONException ex) {
            ex.printStackTrace();
            return new Usuario();
        }


    }
}
