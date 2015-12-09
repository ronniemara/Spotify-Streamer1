package net.africahomepage.ron.spotify_streamer1;

import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by ron on 01/12/15.
 */
public class PlayerFragDialog extends DialogFragment {

    private static int mIndex;
    private static MediaPlayer mMediaPlayer;
    ArrayList<TrackObject> mTracks;
    TrackObject mTrack;
    private onControlMediaPlayer dialogListener;
    String mArtist;
    private int mProgress;
    PlayerOnErrorListener errorListener = null;
    String LOG_TAG = PlayerFragDialog.class.getSimpleName();

    @Bind(R.id.previous_button)        ImageButton prev;
    @Bind(R.id.play_button)            ToggleButton play;
    @Bind(R.id.next_button)            ImageButton next;
    @Bind(R.id.song_player_image_view) ImageView imageView;
    @Bind(R.id.song_artist_text_view)  TextView artistName;
    @Bind(R.id.song_album_text_view)   TextView albumName;
    @Bind(R.id.song_seek_bar)           SeekBar seekBar;
    @Bind(R.id.song_title_text_view)    TextView songTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();

        if(extras != null) {
            if (extras.containsKey("net.africahomepage.ron.Tracks")) {
                mTracks = extras.getParcelableArrayList("net.africahomepage.ron.Tracks");
            }

            if (extras.containsKey("net.africahomepage.ron.index") && !mTracks.isEmpty()) {
                mIndex = extras.getInt("index");
                mTrack = mTracks.get(mIndex);
            }

            if (extras.containsKey("net.africahomepage.ron.artist")) {
                mArtist = extras.getString("net.africahomepage.ron.artist");
            }

        }  else {
            mTracks = getArguments().getParcelableArrayList("net.africahomepage.ron.Tracks");
            mIndex = getArguments().getInt("net.africahomepage.ron.index");
            mTrack = mTracks.get(mIndex);

            mArtist = getArguments().getString("net.africahomepage.ron.artist");
        }

        mProgress = 0;

        //set up MediaPlayer
        errorListener = new PlayerOnErrorListener();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(errorListener);
        mMediaPlayer.setOnPreparedListener(new PlayerOnPreparedListener());

        try{
            mMediaPlayer.setDataSource(mTrack.mPreviewUrl);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, e.getMessage());
        } catch(IOException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch(IllegalStateException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_song_player, null));

        return builder.create();
    }
                        

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                mMediaPlayer.stop();
                mMediaPlayer.release();
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        dialog.cancel();
    }

    public static void setUp(MediaPlayer mediaPlayer, int index) {
        mMediaPlayer = mediaPlayer;
        mIndex = index;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            dialogListener = (onControlMediaPlayer) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()
                    + "needs to implement onControlMediaPlayer");
        }
    }

    public static PlayerFragDialog newInstance(AppCompatActivity context) {


        PlayerFragDialog f = new PlayerFragDialog();
        Bundle extras = context.getIntent().getExtras();

        f.setArguments(extras);

        return f;
    }



    public interface onControlMediaPlayer {
        void  passMediaPlayer(AppCompatActivity activity, MediaPlayer mp, int index);
    }

}

