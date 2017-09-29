package com.utn.mobile.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.utn.mobile.myapplication.MovieGuruApplication;

/**
 * Created by lucho on 29/09/17.
 */

public class NetworkUtil {

    public static boolean hasInternetAccess(){
        Context context = MovieGuruApplication.getAppContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }
}
