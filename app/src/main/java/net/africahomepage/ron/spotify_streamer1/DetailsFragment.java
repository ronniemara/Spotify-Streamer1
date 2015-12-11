package net.africahomepage.ron.spotify_streamer1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    ArrayList<TrackObject> mTracksData = new ArrayList<>();
    ArtistObject mArtist = null;
    DetailsAdapter mAdapter = null;

    private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    static final String TRACK_DATA = "trackData";
    static final String ARTIST_DATA = "artistData";
    private final String LOG_TAG = DetailsFragment.class.getSimpleName();

    public static DetailsFragment newInstance(ArtistObject artist) {
        DetailsFragment d = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("artist", artist);

        d.setArguments(args);

        return d;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //there is no saved state or there is no track data or there is no artist name
        if (savedInstanceState == null || !savedInstanceState.containsKey(TRACK_DATA) ||
                !savedInstanceState.containsKey(ARTIST_DATA)) {
            mTracksData = new ArrayList<>();

        } else {
            mTracksData = savedInstanceState.getParcelableArrayList(TRACK_DATA);
            mArtist = savedInstanceState.getParcelable(ARTIST_DATA);
        }
        Bundle extras = getActivity().getIntent().getExtras();
        // Set title
        if (extras != null) {
            if (extras.containsKey(ARTIST_DATA)) {
                mArtist = extras.getParcelable(ARTIST_DATA);
                if (mArtist != null) {
                    getActivity().setTitle("Top 10 Tracks \n " + mArtist.mName);
                }
            }  else {
                getActivity().setTitle("Search for artist");
            }

        } else{
            extras = getArguments();
            mArtist = extras.getParcelable("artist");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save mTracksData list
        outState.putParcelableArrayList(TRACK_DATA, mTracksData);
        outState.putParcelable(ARTIST_DATA, mArtist);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar_id);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.details_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new DetailsAdapter(mArtist.mName, mTracksData);
        recyclerView.setAdapter(mAdapter);

        /*
        Check if there is a list of tracks and if this fragment is opened by "two-pane" tablet view.
        If fragment is a part of "two-pane" activity return View without trying to fetch data
         */
        if (mTracksData.isEmpty()) {
            startFetchTrackTASk();
        }

        return rootView;
    }

    private void startFetchTrackTASk() {
        FetchTrackTAsk fetchTrackTAsk = new FetchTrackTAsk();
        fetchTrackTAsk.execute();
    }

    public class FetchTrackTAsk extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchTrackTAsk.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(ProgressBar.VISIBLE);
            //mProgress.setProgress(mProgressStatus);
            mProgress.setIndeterminate(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map query = new HashMap();
            query.put("country", "CA");

            Tracks topTracksData = null;

            try {
                topTracksData = spotify.getArtistTopTrack(mArtist.mSpotifyId, query);
            } catch (RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage());
                Toast.makeText(getActivity(), "API error. Could not complete request", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "NullpointerException");
                Toast.makeText(getActivity(), "NullpointerException", Toast.LENGTH_SHORT).show();
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
        protected void onPostExecute(Void tracks) {
            mProgress.setVisibility(ProgressBar.GONE);
            if (mTracksData.isEmpty()) {
                Toast.makeText(getActivity(), "There are no top tracks for the artist selected", Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public void update(ArtistObject artist) {
        mArtist = artist;
        startFetchTrackTASk();
        mAdapter.notifyDataSetChanged();
    }

}
