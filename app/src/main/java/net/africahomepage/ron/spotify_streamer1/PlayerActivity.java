package net.africahomepage.ron.spotify_streamer1;
import android.support.v4.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.View;

import net.africahomepage.ron.spotify_streamer1.PlayerFragDialog;
import net.africahomepage.ron.spotify_streamer1.R;
import net.africahomepage.ron.spotify_streamer1.Util;

import java.util.ArrayList;


/**
 * Created by ron on 08/11/15.
 */
public class PlayerActivity extends AppCompatActivity
        implements PlayerFragDialog.onControlMediaPlayer
 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song_player);

        Util.showFragment(this);
    }

     @Override
     public void passMediaPlayer(AppCompatActivity activity, MediaPlayer mp, int index) {
         new Util().passMediaPlayer(activity,mp,index);
     }
 }
