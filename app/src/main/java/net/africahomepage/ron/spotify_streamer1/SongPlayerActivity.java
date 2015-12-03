package net.africahomepage.ron.spotify_streamer1;

import android.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ron on 08/11/15.
 */
public class SongPlayerActivity extends AppCompatActivity
        implements SongPlayerCtrlDialog.onControlMediaPlayer {

    static ArrayList<TrackObject> mTracks= null;
    static TrackObject mTrack = null;
    static MediaPlayer mMediaPlayer = null;
    String mArtist;
    MediaPlayerOnErrorListener errorListener = null;
    static public int mIndex = -10;
    String LOG_TAG = SongPlayerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get tracks
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("net.africahomepage.ron.Tracks")) {
            mTracks = extras.getParcelableArrayList("net.africahomepage.ron.Tracks");
        }

        if(extras.containsKey("net.africahomepage.ron.index") && !mTracks.isEmpty()) {
            mIndex = extras.getInt("index");
            mTrack = mTracks.get(mIndex);
        }

        if(extras.containsKey("net.africahomepage.ron.artist")) {
            mArtist = extras.getString("net.africahomepage.ron.artist");
        }

        //set up MediaPlayer
        errorListener = new MediaPlayerOnErrorListener();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(errorListener);
        mMediaPlayer.setOnPreparedListener(new MediaPlayerOnPreparedListener());

        try{
            mMediaPlayer.setDataSource(mTrack.mPreviewUrl);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, e.getMessage());
        } catch(IOException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch(IllegalStateException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
        }

        setContentView(R.layout.activity_song_player);


        showFragment();


    }



    public void showFragment() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SongPlayerCtrlDialog  newFragment = SongPlayerCtrlDialog.newInstance();
        newFragment.show(ft, "dialog");
    }

    @Override
    public void passMediaPlayer() {
        SongPlayerCtrlDialog playerDialog =(SongPlayerCtrlDialog)
                this.getSupportFragmentManager()
                        .findFragmentById(R.id.song_player_dialog_fragment);
        playerDialog.setUp(mMediaPlayer, mIndex);
    }
}
