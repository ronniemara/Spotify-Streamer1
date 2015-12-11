package net.africahomepage.ron.spotify_streamer1;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ron on 03/12/15.
 */
public class Util         {
    
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting();

    }



    public static void showFragment(AppCompatActivity context) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        View details = context.findViewById(R.id.details);
         boolean dualPane = details != null && details.getVisibility() == View.VISIBLE;

        if(dualPane) {
            FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
            Fragment prev = context.getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            PlayerFragDialog newFragment = PlayerFragDialog.newInstance(context);
            newFragment.show(ft, "dialog");
        } else {
            FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();

            PlayerFragDialog newFragment = PlayerFragDialog.newInstance(context);
            ft.add(R.id.player_activity, newFragment);
            ft.commit();

        }
    }




}
