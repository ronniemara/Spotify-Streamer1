package net.africahomepage.ron.spotify_streamer1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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
public class DetailsActivityFragment extends Fragment {

    ArrayList<TrackObject> mTracksData = new ArrayList<>();
    String mSpotifyId = null;
    DetailsAdapter mAdpater = null;
    Bundle extras = null;
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    static final String TRACK_DATA = "trackData";


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

        if(savedInstanceState != null && savedInstanceState.containsKey(TRACK_DATA) ) {
            mTracksData = savedInstanceState.getParcelableArrayList(TRACK_DATA);
        }


        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save mTracksData list
        outState.putParcelableArrayList(TRACK_DATA, mTracksData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mProgress  = (ProgressBar) rootView.findViewById(R.id.progress_bar_id);


        if (extras != null) {
            mSpotifyId = extras.getString(Intent.EXTRA_TEXT);
        }

        if(mTracksData.isEmpty()) {
            startFetchTrackTASk();
        }

        mAdpater = new DetailsAdapter(getActivity(), mTracksData);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.details_listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mAdpater);

        return rootView;
    }

    private void startFetchTrackTASk() {
        FetchTrackTAsk fetchTrackTAsk = new FetchTrackTAsk(mProgress,mProgressStatus, mSpotifyId, getActivity(), mAdpater, mTracksData);
        fetchTrackTAsk.execute();

    }

}
