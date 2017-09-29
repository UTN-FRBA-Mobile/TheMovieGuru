package com.utn.mobile.myapplication.cache;

/**
 * Created by lucho on 29/09/17.
 */

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.utn.mobile.myapplication.MovieGuruApplication;
import com.utn.mobile.myapplication.interfaces.Closure;
import com.utn.mobile.myapplication.utils.NetworkUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheClient {

    private static final CacheClient INSTANCE = new CacheClient();
    private static long TIME_TO_LIVE = 360000;
    private static Map<String, Object> memoryCache = new HashMap<>();

    public static CacheClient get() {
        return INSTANCE;
    }

    private CacheClient() {}


    public String get(String key, Closure apiRequest) {
        String value;

        if (exists(key)) {
            value = readFile(key);
        } else {
            value = (String)apiRequest.call();
            put(key, value);
        }

        return value;
    }

    public boolean exists(String key) {
        Context context = MovieGuruApplication.getAppContext();
        File file = new File(context.getFilesDir(), key);
        boolean validData = System.currentTimeMillis() - file.lastModified() < TIME_TO_LIVE;
        return file.exists() && (validData || !NetworkUtil.hasInternetAccess());
    }

    public void invalidFile(String key) {
        if (exists(key)) {
            Context context = MovieGuruApplication.getAppContext();
            File file = new File(context.getFilesDir(), key);
            file.delete();
        }
    }

    public String readFile(String name) {
        Context context = MovieGuruApplication.getAppContext();
        File file = new File(context.getFilesDir(), name);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.i(this.getClass().getName(), "Failed to read file: " + name);
            e.printStackTrace();
        }

        return text.toString();
    }

    private void put(String key, String value) {
        Context context = MovieGuruApplication.getAppContext();
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
            outputStream.write(value.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.i(this.getClass().getName(), "Failed to write file: " + key);
            e.printStackTrace();
        }
    }

    // Avoid putting large amounts of info in memory
    public void putInMemory(String key, Object value) {
        memoryCache.put(key, value);
    }

    public Object getFromMemory(String key) {
        return memoryCache.get(key);
    }

    public void clearCache() {
        Context context = MovieGuruApplication.getAppContext();
        File[] files = context.getFilesDir().listFiles();

        for (File oneFile: files) {
            Log.i(getClass().getName(), "ARCHIVO = " + oneFile.getAbsolutePath());
            //oneFile.delete();
        }
    }

}

