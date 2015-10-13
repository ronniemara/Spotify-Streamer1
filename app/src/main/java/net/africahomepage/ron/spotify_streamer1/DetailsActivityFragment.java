package net.africahomepage.ron.spotify_streamer1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements OnTaskCompletedListener {

    ArrayList<TrackObject> mTracksData = new ArrayList<>();
    TracksAdapter mAdpater = null;
    ArtistObject mArtist = null;


    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    static final String TRACK_DATA = "trackData";

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null && savedInstanceState.containsKey(TRACK_DATA)) {
            mTracksData = savedInstanceState.getParcelableArrayList(TRACK_DATA);
        }

        Bundle extras = getActivity().getIntent().getExtras();
        // Set title
        if (extras != null && extras.containsKey("net.africahomepage.ron.spotify_streamer1.artist")) {
            mArtist = extras.getParcelable("net.africahomepage.ron.spotify_streamer1.artist");
            StringBuilder titleBuilder = new StringBuilder("Top 10 Tracks \n ");
            titleBuilder.append(mArtist.mName);

            getActivity().setTitle(titleBuilder.toString());
        }

        mAdpater = new TracksAdapter(getActivity(), mTracksData);

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
        View rootView = inflater.inflate(R.layout.fragment_details, container, true);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar_id);

        if (mTracksData.isEmpty()) {
            startFetchTrackTASk();
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.details_listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mAdpater);

        return rootView;
    }

    private void startFetchTrackTASk() {
        FetchTracksTAsk fetchTracksTAsk = new FetchTracksTAsk(this,mProgress, mProgressStatus, mArtist.mSpotifyId, getActivity(), mAdpater, mTracksData);
        fetchTracksTAsk.execute();

    }

    @Override
    public void taskCompleted() {

        if (mTracksData.isEmpty()) {
            Toast.makeText(getActivity(), "No artist found. Please refine your search", Toast.LENGTH_SHORT).show();
        }
    }


}
