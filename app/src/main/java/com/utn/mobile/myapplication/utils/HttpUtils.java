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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.entity.StringEntity;

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
        return pruebaPost(endpoint, params, "POST", authentication);
    }

    public static String postEncoded(String endpoint, String params)
            throws IOException {
        return urlEncodedRequest(endpoint, params, "POST", false);
    }

    public static String put(String endpoint, JSONObject params, boolean authentication)
            throws IOException {
        return request(endpoint, params, "PUT", authentication);
    }

    public static String delete(String endpoint)
            throws IOException {
        return deleteItem(endpoint, "DELETE", false);
    }

    public static String delete(String endpoint, boolean authentication)
            throws IOException {
        return deleteItem(endpoint, "DELETE", authentication);
    }

    public static String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public static String urlEncodedRequest(String endpoint, String params, String method, boolean authentication)
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
            byte[] postData = params.getBytes( Charset.forName("UTF-8") );
            int postDataLength = postData.length;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-length", Integer.toString(postDataLength ));
            if(authentication){
                Context context = MovieGuruApplication.getAppContext();
                String token = PreferenceManager.getDefaultSharedPreferences(context).getString("user-token", "");
                conn.setRequestProperty ("Authorization", "Bearer " + token);

            }
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
            wr.write( postData );
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

    public static String pruebaPost(String endpoint, JSONObject params, String method, boolean secure) throws IOException {
        HttpURLConnection urlConnection = null;
        String response;
        try {
            URL url = new URL(endpoint);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setDoInput(true);

            if (secure) {
                Context context = MovieGuruApplication.getAppContext();
                String token = PreferenceManager.getDefaultSharedPreferences(context).getString("user-token", "");
                urlConnection.setRequestProperty ("Authorization", "Bearer " + token);
            }

            if (params != null) {
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                //urlConnection.setFixedLengthStreamingMode(params.toString().length());
                urlConnection.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
                StringEntity se = new StringEntity(params.toString(), "UTF-8");
                se.writeTo(dos);
                dos.flush();
                dos.close();
            }

            int status = urlConnection.getResponseCode();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = readStream(in);
            if (status != 200) {
                throw new IOException(method + " failed with error code " + status);
            }
            return response;

        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    public static String deleteItem(String endpoint, String method, boolean authentication)
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
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            if(authentication){
                Context context = MovieGuruApplication.getAppContext();
                /*
                Token de logueo
                String token = PreferenceManager.getDefaultSharedPreferences(context).getString(SignInPreferences.AUTH_SERVER_TOKEN, "");
                conn.setRequestProperty ("Authorization", "Token token=" + token);
                */
            }
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
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
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
