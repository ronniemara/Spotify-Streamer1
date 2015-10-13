package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * Created by ron on 08/10/15.
 */
public class FetchTracksTAsk extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = FetchTracksTAsk.class.getSimpleName();
    public OnTaskCompletedListener mListener = null;
    private ProgressBar mProgress;
    ArrayList<TrackObject> mTracksData = null;
    String mSpotifyId = "";
    Context mContext = null;
    TracksAdapter mAdapter = null;
    int mProgressStatus;

    public FetchTracksTAsk(OnTaskCompletedListener listener, ProgressBar progressBar, int progressStatus, String spotifyId, Context context, TracksAdapter adapter, ArrayList<TrackObject> tracks) {
        mProgress =progressBar;
        mSpotifyId = spotifyId;
        mContext = context;
        mTracksData = tracks;
        mAdapter = adapter;
        mProgressStatus =progressStatus;
        mListener = listener;

    }

    @Override
    protected void onPreExecute() {
        mProgress.setVisibility(ProgressBar.VISIBLE);
        mProgress.setProgress(mProgressStatus);
        mProgress.setIndeterminate(true);
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map query = new HashMap();
        query.put("country", "CA");

        Tracks topTracksData =  null;

        try {
            topTracksData = spotify.getArtistTopTrack(mSpotifyId, query);

        } catch(RetrofitError error) {
            Log.e(LOG_TAG, error.getMessage().toString());
            Toast.makeText(mContext, "API error. Could not complete request", Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullpointerException");
            Toast.makeText(mContext, "NullpointerException", Toast.LENGTH_SHORT).show();
        }

        List<Track> tracksList = topTracksData.tracks;
        if (tracksList.isEmpty()) {
            return null;
        }
        mTracksData.clear();

        for (int i = 0; i < tracksList.size(); i++) {
            Track track = tracksList.get(i);
            String albumSmallImage = track.album.images.get(1).url;
            String albumLargeImage = track.album.images.get(0).url;

            mTracksData.add(new TrackObject(track.album.name, track.name, albumSmallImage, albumLargeImage, track.preview_url));
        }


        return null;

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
        mProgress.setVisibility(ProgressBar.GONE);
        mListener.taskCompleted();

        mAdapter.notifyDataSetChanged();


    }
}