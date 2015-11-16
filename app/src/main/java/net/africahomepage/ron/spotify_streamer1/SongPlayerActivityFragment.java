package net.africahomepage.ron.spotify_streamer1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;


import android.widget.Toast;
import android.media.AudioManager;
import android.util.Log;

import java.io.IOException;



import com.squareup.picasso.Picasso;


/**
 * Created by ron on 08/11/15.
 */
public class SongPlayerActivityFragment extends Fragment{

    TrackObject mTrack= null;
    MediaPlayer mMediaPlayer = null;
    MediaPlayerOnErrorListener errorListener = null;
    public final String LOG_TAG = SongPlayerActivityFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras.containsKey("net.africahomepage.ron.Track")) {
            mTrack = extras.getParcelable("net.africahomepage.ron.Track");
        }

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
        View view = inflater.inflate(R.layout.fragment_song_player, container);
        ImageView imageView = (ImageView) view.findViewById(R.id.song_player_image_view);
        Picasso.with(getActivity()).load(mTrack.mTrackLargeImageUrl).into(imageView);

        TextView titleTextView = (TextView) view.findViewById(R.id.song_title_text_view);
        titleTextView.setText(mTrack.mTrackTitle);

        TextView albumTextView = (TextView) view.findViewById(R.id.song_title_text_view);
        albumTextView.setText(mTrack.mTrackAlbum);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                mMediaPlayer.stop();
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void previous() {

    }


    public void pause(View View) {
        mMediaPlayer.pause();
    }


    public void next() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
