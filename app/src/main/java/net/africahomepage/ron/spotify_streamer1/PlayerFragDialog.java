package net.africahomepage.ron.spotify_streamer1;

import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.support.annotation.Nullable;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ron on 01/12/15.
 */
public class PlayerFragDialog extends DialogFragment {

    private static int mIndex;
    private static MediaPlayer mMediaPlayer;
    public MediaController mMediaController;
    ArrayList<TrackObject> mTracks;
    TrackObject mTrack;
    String mArtist;
    PlayerOnErrorListener errorListener = null;
    String LOG_TAG = PlayerFragDialog.class.getSimpleName();
    boolean mIsLargeLayout;

    @Bind(R.id.song_player_image_view) ImageView imageView;
    @Bind(R.id.song_artist_text_view)  TextView artistName;
    @Bind(R.id.song_album_text_view)   TextView albumName;

    @Bind(R.id.song_title_text_view)    TextView songTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsLargeLayout = getActivity().getResources().getBoolean(R.bool.large_layout);

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
//        Log.i(LOG_TAG, "In onCreateDialog");
//
//        mMediaController = new MediaController(getActivity());
//
//
//
//
//        //mMediaController.setAnchorView(rootView);
//        mMediaController.setMediaPlayer(MyMediaPlayer.newInstance(mMediaPlayer));
//
//        //set up MediaPlayer
//        errorListener = new PlayerOnErrorListener();
//        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setOnErrorListener(errorListener);
//        mMediaPlayer.setOnPreparedListener(new PlayerOnPreparedListener());
//
//        try{
//            mMediaPlayer.setDataSource(mTrack.mPreviewUrl);
//            mMediaPlayer.prepareAsync();
//        } catch (IllegalArgumentException e) {
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e(LOG_TAG, e.getMessage());
//        } catch(IOException e) {
//            Log.e(LOG_TAG,e.getMessage());
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        } catch(IllegalStateException e) {
//            Log.e(LOG_TAG,e.getMessage());
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
//        }


//        mMediaController.show();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song_player, container, false);
        ButterKnife.bind(this, rootView);

        Picasso.with(getActivity())

                .load(mTrack.mTrackLargeImageUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .fit()
                .into(imageView);

        artistName.setText(mArtist.toString());
        albumName.setText(mTrack.mTrackAlbum);
        songTitle.setText(mTrack.mTrackTitle);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStance) {
        Dialog dialog = super.onCreateDialog(savedInstanceStance);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

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


    public static PlayerFragDialog newInstance(AppCompatActivity context) {


        PlayerFragDialog f = new PlayerFragDialog();
        Bundle extras = context.getIntent().getExtras();

        f.setArguments(extras);

        return f;
    }
}

