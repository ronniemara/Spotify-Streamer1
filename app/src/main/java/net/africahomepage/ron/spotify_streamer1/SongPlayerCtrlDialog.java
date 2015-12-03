package net.africahomepage.ron.spotify_streamer1;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ron on 01/12/15.
 */
public class SongPlayerCtrlDialog extends DialogFragment {

    private static int mIndex;
    private static MediaPlayer mMediaPlayer;
    ArrayList<TrackObject> mTracks;
    TrackObject mTrack;
    private onControlMediaPlayer dialogListener;
    String mArtist;
    private int mProgress;


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
        mTracks = extras.getParcelableArrayList("net.africahomepage.ron.Tracks");
        mIndex = extras.getInt("net.africahomepage.ron.index");
        mTrack = mTracks.get(mIndex);
        mArtist = extras.getString("net.africahomepage.ron.artist");
        mProgress = 0;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.songplayer_frag_dialog, null));

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

    public static SongPlayerCtrlDialog newInstance() {
        SongPlayerCtrlDialog f = new SongPlayerCtrlDialog();
        return f;
    }



    public interface onControlMediaPlayer {
        void passMediaPlayer();
    }

}

