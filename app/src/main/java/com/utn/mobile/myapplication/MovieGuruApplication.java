package com.utn.mobile.myapplication;

/**
 * Created by lucho on 29/09/17.
 */

import android.app.Application;
import android.content.Context;

public class MovieGuruApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MovieGuruApplication.context = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        // Solves compatibility issues with old versions of android
        // See https://developer.android.com/reference/android/support/multidex/MultiDex.html
        super.attachBaseContext(base);
    }

    public static Context getAppContext() {
        return MovieGuruApplication.context;
    }
}