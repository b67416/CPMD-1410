package com.ryanwahle.projecthours;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetStatus {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        return true;
    }

    public static void showNoInternetAlert(Context context) {
        AlertDialog.Builder noInternetDialog = new AlertDialog.Builder(context);
        noInternetDialog.setTitle("Internet Connection Error");
        noInternetDialog.setMessage("Please connect to the internet and try again!");
        noInternetDialog.setNegativeButton("OK", null);
        noInternetDialog.setCancelable(false);
        noInternetDialog.show();
    }
}
