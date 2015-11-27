package net.africahomepage.ron.spotify_streamer1;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {


    public OnUpdateUIListener mCallback = null;

    ArtistAdapter mSpotifyadapter = null;
    ArrayList<ArtistObject> mArtistData = new ArrayList<>();

    public MainFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ArtistDAta", mArtistData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey("ArtistDAta")) {
            mArtistData = new ArrayList<>();
        } else {
            mArtistData = savedInstanceState.getParcelableArrayList("ArtistDAta");

        }

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
                "us-east-1:6d54f99d-7587-40d5-8a15-1fb02a6fafaa", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getActivity().getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

// Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
            }
        });


        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUpdateUIListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);


        final SearchView searchEditText = (SearchView) root.findViewById(R.id.search_editText);

        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEditText.clearFocus();
                OnTaskCompletedListiner listiner = new OnTaskCompletedListiner() {
                    @Override
                    public void taskCompleted() {
                        if (mArtistData.isEmpty()) {
                            Toast.makeText(getActivity(), "No artist found. Please refine your search", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                ConnectivityManager cm =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    FetchMusicTask fetchMusicTask = new FetchMusicTask(listiner);
                    fetchMusicTask.execute(query);

                } else {
                    Toast.makeText(getActivity(), "There is no internet connection. Please try again when you have access to the internet.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSpotifyadapter = new ArtistAdapter(getActivity(), mArtistData);


        RecyclerView recycView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recycView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycView.setAdapter(mSpotifyadapter);


        return root;
    }

    public class FetchMusicTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchMusicTask.class.getSimpleName();
        public OnTaskCompletedListiner listener = null;


        public FetchMusicTask(OnTaskCompletedListiner listener) {
            this.listener = listener;
        }

        protected void onPostExecute(Void result) {

            listener.taskCompleted();

            mSpotifyadapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... artist) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager artistsData = spotify.searchArtists(artist[0]);
            List<Artist> artistInfo = artistsData.artists.items;

            mArtistData.clear();

            if (artistInfo.isEmpty()) {
                return null;
            }

            for (int i = 0; i < artistInfo.size(); i++) {
                Artist art = artistInfo.get(i);
                List<Image> images = art.images;
                mArtistData.add(new ArtistObject(
                        art.name,
                        art.id,
                        images.isEmpty() ? null : images.get(0).url
                ));
            }

            return null;
        }
    }


    public interface OnTaskCompletedListiner {
        void taskCompleted();
    }

    public interface OnUpdateUIListener {
        void updateUI(ArtistObject artist);
    }

    public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private List<ArtistObject> mValues;
        private int mBackground;
        public String LOG_TAG = ArtistAdapter.class.getSimpleName();

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mArtistNAme;
            public final ImageView mImageview;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mArtistNAme = (TextView) view.findViewById(R.id.artist_name_textview);
                mImageview = (ImageView) view.findViewById(R.id.artist_imageview);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.main_listview_textview, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {

            final ArtistObject artist = mValues.get(i);
            viewHolder.mArtistNAme.setText(artist.mName);

            Picasso
                    .with(viewHolder.mImageview.getContext())
                    .load(artist.mImageUrl)
                    .fit()
                    .centerCrop()
                    .into(viewHolder.mImageview);


            viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallback.updateUI(artist);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public ArtistAdapter(Context context, List<ArtistObject> artistObjects) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = artistObjects;
        }

    }


}



