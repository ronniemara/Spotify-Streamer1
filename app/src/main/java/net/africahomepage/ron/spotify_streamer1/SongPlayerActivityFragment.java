package net.africahomepage.ron.spotify_streamer1;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;


import android.widget.Toast;
import android.media.AudioManager;
import android.util.Log;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;


import com.squareup.picasso.Picasso;


/**
 * Created by ron on 08/11/15.
 */
public class SongPlayerActivityFragment extends Fragment{

    ArrayList<TrackObject> mTracks= null;
    TrackObject mTrack = null;
    MediaPlayer mMediaPlayer = null;
    MediaPlayerOnErrorListener errorListener = null;
    public final String LOG_TAG = SongPlayerActivityFragment.class.getSimpleName();
    public int mIndex = -10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras.containsKey("net.africahomepage.ron.Tracks")) {
            mTracks = extras.getParcelableArrayList("net.africahomepage.ron.Tracks");
            mIndex = (Integer) extras.get("index");
        }
        mTrack = mTracks.get(mIndex);
        errorListener = new MediaPlayerOnErrorListener();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(errorListener);
        mMediaPlayer.setOnPreparedListener(new MediaPlayerOnPreparedListener());

        try{
            mMediaPlayer.setDataSource(mTrack.mPreviewUrl);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG,e.getMessage());
        } catch(IOException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch(IllegalStateException e) {
            Log.e(LOG_TAG,e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
        }

        getActivity().setTitle(mTrack.mTrackTitle + " from album " + mTrack.mTrackAlbum);

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_song_player, container);
        ImageView imageView = (ImageView) view.findViewById(R.id.song_player_image_view);
        Picasso
                .with(getActivity())
                .load(mTrack.mTrackLargeImageUrl)
                .fit().into(imageView);

        TextView titleTextView = (TextView) view.findViewById(R.id.song_title_text_view);
        titleTextView.setText(mTrack.mTrackTitle);

        TextView albumTextView = (TextView) view.findViewById(R.id.song_title_text_view);
        albumTextView.setText(mTrack.mTrackAlbum);

        final ToggleButton playButton = (ToggleButton) view.findViewById(R.id.play_button);
        playButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    mMediaPlayer.pause();
                    mMediaPlayer.release();
                    playButton.setButtonDrawable(android.R.drawable.ic_media_play);
                } else {
                    // The toggle is disabled
                    mMediaPlayer.start();
                    playButton.setButtonDrawable(android.R.drawable.ic_media_pause);
                }
            }
        });

        ImageButton prevButton = (ImageButton) view.findViewById(R.id.previous_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newIndex = mIndex - 1;
                if (newIndex < 0 || newIndex > 9) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("index", newIndex);
                    intent.putParcelableArrayListExtra("net.africahomepage.ron.Tracks", mTracks);
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    getActivity().finish();
                    startActivity(intent);
                }

            }
        });

        ImageButton nextButton = (ImageButton) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newIndex = mIndex + 1;
                if (newIndex < 0 || newIndex > 9) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    NavUtils.navigateUpFromSameTask(getActivity());
                } else {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra("index", newIndex);
                    intent.putParcelableArrayListExtra("net.africahomepage.ron.Tracks", mTracks);
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    getActivity().finish();
                    startActivity(intent);
                }

            }
        });

        return view;
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


    public void next() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
