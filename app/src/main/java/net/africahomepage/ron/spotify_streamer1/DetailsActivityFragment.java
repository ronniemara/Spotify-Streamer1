package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
public class DetailsActivityFragment extends Fragment implements OnTaskCompletedListener {

    ArrayList<TrackObject> mTracksData = new ArrayList<>();
    TracksAdapter mAdpater = null;
    ArtistObject mArtist = null;

    final String LOG_TAG = "DetailsActivityFragment";


    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    static final String TRACK_DATA = "trackData";

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(TRACK_DATA)) {
            mTracksData = savedInstanceState.getParcelableArrayList(TRACK_DATA);
        }

        Intent intent = getActivity().getIntent();
        intent.setExtrasClassLoader(ArtistObject.class.getClassLoader());

        //Bundle extras = intent.getExtras();

        // Set title
        //if (extras != null && extras.containsKey("net.africahomepage.ron.spotify_streamer1.artist")) {
        mArtist = intent.getParcelableExtra("net.africahomepage.ron.spotify_streamer1.artist");
        StringBuilder titleBuilder = new StringBuilder("Top 10 Tracks \n ");
        titleBuilder.append(mArtist.mName);

        getActivity().setTitle(titleBuilder.toString());
        //}

        mAdpater = new TracksAdapter(getActivity(), mTracksData);



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
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar_id);

        if (mTracksData.isEmpty()) {
            startFetchTrackTASk();
        }

        Log.d(LOG_TAG, mTracksData.isEmpty() ? "is empty" : "is not empty");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.details_listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(mAdpater);

        return rootView;
    }

    private void startFetchTrackTASk() {
        FetchTracksTAsk fetchTracksTAsk = new FetchTracksTAsk();
        fetchTracksTAsk.execute();

    }

    @Override
    public void taskCompleted() {

        if (mTracksData.isEmpty()) {
            Toast.makeText(getActivity(), "No top ten tracks found. Please refine your search", Toast.LENGTH_SHORT).show();
        }
    }

    public class FetchTracksTAsk extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchTracksTAsk.class.getSimpleName();


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

                topTracksData = spotify.getArtistTopTrack(mArtist.mSpotifyId, query);


            } catch(RetrofitError error) {
                Log.e(LOG_TAG, error.getMessage().toString());
                Toast.makeText(getActivity(), "API error. Could not complete request", Toast.LENGTH_SHORT).show();
            }
            catch (NullPointerException e) {
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

            taskCompleted();

            Log.d(LOG_TAG, mTracksData.get(0).mTrackTitle);
            mAdpater.notifyDataSetChanged();
        }
    }

    public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

        private List<TrackObject> mTrackData;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground=0;


        public TracksAdapter(Context context, List<TrackObject> trackObjects) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mTrackData = trackObjects;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.details_listview_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final TrackObject track = mTrackData.get(position);
            holder.mTrackTitle.setText(track.mTrackTitle);

            Picasso.with(holder.mImageview.getContext())
                    .load(track.mTrackSmallImageUrl)
                    .into(holder.mImageview);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SongPlayerActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("net.africahomepage.ron.spotify_streamer1.track", track);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            ImageView mImageview;
            TextView mTrackTitle;
            TextView mTrackAlbum;

            public ViewHolder(View view) {
                super(view);

                mView = view;
                mImageview = (ImageView) view.findViewById(R.id.details_listview_layout_imageview);
                mTrackTitle = (TextView) view.findViewById(R.id.details_listview_layout_song);
                mTrackAlbum = (TextView) view.findViewById(R.id.details_listview_layout_album);

            }
        }
    }


}
