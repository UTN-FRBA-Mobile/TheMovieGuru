package com.utn.mobile.myapplication.utils;

import android.content.Context;
import android.graphics.Movie;
import android.preference.PreferenceManager;

import com.utn.mobile.myapplication.MovieGuruApplication;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lucho on 29/09/17.
 */

public class HttpUtils {

    public static String post(String endpoint, JSONObject params)
            throws IOException {
        return request(endpoint, params, "POST", false);
    }

    public static String post(String endpoint, JSONObject params, boolean authentication)
            throws IOException {
        return request(endpoint, params, "POST", authentication);
    }

    public static String put(String endpoint, JSONObject params, boolean authentication)
            throws IOException {
        return request(endpoint, params, "PUT", authentication);
    }

    public static String request(String endpoint, JSONObject params, String method, boolean authentication)
            throws IOException {

        URL url;
        String response;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setRequestProperty("Content-length", Integer.toString(params.toString().length()));
            if(authentication){
                Context context = MovieGuruApplication.getAppContext();
                /*
                Token de logueo
                String token = PreferenceManager.getDefaultSharedPreferences(context).getString(SignInPreferences.AUTH_SERVER_TOKEN, "");
                conn.setRequestProperty ("Authorization", "Token token=" + token);
                */
            }
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
            wr.writeBytes(params.toString());
            wr.close();
            int status = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = readStream(in);
            if (status != 200) {
                throw new IOException(method + " failed with error code " + status);
            }
            return response;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
