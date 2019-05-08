package com.maruf.tourmate;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckConnectivity {

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

}
