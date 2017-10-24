package com.utn.mobile.myapplication.service;

import com.utn.mobile.myapplication.R;
import com.utn.mobile.myapplication.domain.Imagen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class ImageService extends AbstractService {


    //Singleton
    private static final ImageService INSTANCE = new ImageService();
    public static ImageService get() {
        return INSTANCE;
    }

    private ImageService() {
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    }

    public String getOne(int id) { return getOne(false, id); }

    public String getOne(boolean authentication, int id) {
        String url = context.getString(R.string.other_api_actor);
        String key = context.getString(R.string.cache_key_image)+id;
        String actorId = String.valueOf(id);
        String img_url = (String) get(url + actorId+"/images?api_key=acc843cef0728308b8fa8b96c251f206", key, authentication);
        return img_url;
    }

    @Override
    protected String deserialize(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            List<String> imagenes = new ArrayList<>();
            JSONArray imagesJsonArray = jsonObject.getJSONArray("profiles");

            if(imagesJsonArray.length() == 0)
            {
                return "";
            }
            else
            {
                return imagesJsonArray.getJSONObject(0).getString("file_path");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
