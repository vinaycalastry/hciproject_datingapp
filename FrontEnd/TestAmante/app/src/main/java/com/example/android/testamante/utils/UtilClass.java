package com.example.android.testamante.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by suresh on 11/19/17.
 */

public class UtilClass {

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public static void showToast(Context ctx, String message)
    {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}
