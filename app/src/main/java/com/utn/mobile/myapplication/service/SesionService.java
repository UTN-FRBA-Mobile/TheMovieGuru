package com.utn.mobile.myapplication.service;

import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Usuario;
import com.utn.mobile.myapplication.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.HashMap;


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
        //validemos algo pls
        String url = String.format(context.getString(R.string.url_login),
                                    context.getString(R.string.base_url));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", nombreDeUsuario);
        params.put("password", contraseña);
        try {
            String urlParameters  = HttpUtils.getDataString(params);
            String response = this.postUrlEncoded(url, urlParameters);
            u = (Usuario) deserialize(response);
            return u;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
