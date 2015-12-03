package net.africahomepage.ron.spotify_streamer1;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ron on 03/12/15.
 */
public class Util {
    
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();

    }
}
