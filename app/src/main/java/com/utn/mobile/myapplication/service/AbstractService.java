package com.utn.mobile.myapplication.service;

import android.content.Context;
import android.preference.PreferenceManager;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.utn.mobile.myapplication.MovieGuruApplication;
import com.utn.mobile.myapplication.cache.CacheClient;
import com.utn.mobile.myapplication.interfaces.Closure;
import com.utn.mobile.myapplication.utils.GlobalConstants;

import static com.utn.mobile.myapplication.utils.HttpUtils.post;
import static com.utn.mobile.myapplication.utils.HttpUtils.urlEncodedRequest;

/**
 * Created by lucho on 29/09/17.
 */

public abstract class AbstractService {

    protected Context context = MovieGuruApplication.getAppContext();
    protected CacheClient cacheClient = CacheClient.get();
    protected RestTemplate restTemplate = new RestTemplate();
    protected Gson gson = new GsonBuilder()
            .setDateFormat(GlobalConstants.HOUR_MINUTE_FORMAT)
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 150;

    protected abstract Object deserialize(String json);

    protected Object get(final String url, final String key) {
        return get(url, key, false, false);
    }

    protected Object get(final String url, final String key, boolean storeInMemory) {
        return get(url, key, storeInMemory, false);
    }

    protected String postUrlEncoded(String endpoint, String params){
        String response = null;
        Random random = new Random();
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(100);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                response = urlEncodedRequest(endpoint, params, "POST", false);
                return response;
            } catch (IOException e) {
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
                backoff *= 2;
            }
        }
        return response;
    }

    protected String postAuthenticated(String endpoint, JSONObject params){
        String response = null;
        Random random = new Random();
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(100);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            try {
                response = post(endpoint, params, true);
                return response;
            } catch (IOException e) {
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
                backoff *= 2;
            }
        }
        return response;
    }

    protected Object get(final String url, final String key, boolean storeInMemory, final boolean authentication) {
        // Let's check in memory first
        Object result = cacheClient.getFromMemory(key);
        if(result != null) return result;

        // If it's not there, we will get it from an api call (that can be cached)
        Closure apiCall = new Closure() {
            @Override
            public Object call() {
                if(authentication) {
                    HttpHeaders headers = new HttpHeaders();
                    Context context = MovieGuruApplication.getAppContext();
                    // obtener token de logueo
                    //String token = PreferenceManager.getDefaultSharedPreferences(context).getString(SignInPreferences.AUTH_SERVER_TOKEN, "");
                    // headers.set("Authorization", "Token token=" + token);

                    HttpEntity entity = new HttpEntity(headers);
                    ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                    if (responseEntity.getStatusCode() == HttpStatus.OK){
                        return responseEntity.getBody();
                    }
                    return null; //TODO: handle 401, ask for authentication and rerun this request.
                }
                else{
                    return restTemplate.getForObject(url, String.class);
                }
            }

        };

        result = deserialize(cacheClient.get(key, apiCall));
        if(storeInMemory) {
            cacheClient.putInMemory(key, result);
        }
        return result;
    }
}
