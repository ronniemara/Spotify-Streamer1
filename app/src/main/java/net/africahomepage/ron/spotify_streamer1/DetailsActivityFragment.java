package net.africahomepage.ron.spotify_streamer1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    List<TrackObject> mTracksData = new ArrayList<>();
    String mSpotifyId = null;
    DetailsAdapter adapter = null;
    Bundle extras = null;


    private final String LOG_TAG = DetailsActivityFragment.class.getSimpleName();

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        extras = getActivity().getIntent().getExtras();
        // Set title
        if (extras.containsKey("Artist")) {
            StringBuilder titleBuilder = new StringBuilder("Top 10 Tracks \n ");
            titleBuilder.append(extras.get("Artist"));

             getActivity().setTitle(titleBuilder.toString());
        }
        super.onCreate(savedInstanceState);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        if (extras != null) {
            mSpotifyId = extras.getString(Intent.EXTRA_TEXT);
        }
        startFetchTrackTASk();

        ListView listView = (ListView) rootView.findViewById(R.id.details_listview);
        adapter = new DetailsAdapter(getActivity(), mTracksData);
        listView.setAdapter(adapter);

        return rootView;
    }

    private void startFetchTrackTASk() {
        FetchTrackTAsk fetchTrackTAsk = new FetchTrackTAsk();
        fetchTrackTAsk.execute();


    }

    public class FetchTrackTAsk extends AsyncTask<Void, Void, Void> {
        private final String LOG_TAG = FetchTrackTAsk.class.getSimpleName();

        @Override
        protected void onPostExecute(Void tracks) {
            if (mTracksData.isEmpty()) {
                Toast.makeText(getActivity(), "There are no top tracks for the artist selected", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map query = new HashMap();
            query.put("country", "CA");
            Tracks topTracksData = spotify.getArtistTopTrack(mSpotifyId, query);
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
    }


}
