package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
public class MainActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArtistAdapter mSpotifyadapter = null;
    ArrayList<ArtistObject> mArtistData = new ArrayList<>();

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ArtistDAta", mArtistData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null || !savedInstanceState.containsKey("ArtistDAta")) {
            mArtistData = new ArrayList<>();
        }
        else {
            mArtistData = savedInstanceState.getParcelableArrayList("ArtistDAta");

        }
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query

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

            private final String LOG_TAG =  FetchMusicTask.class.getSimpleName();
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

    public static class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private List<ArtistObject> mValues;
        private int mBackground;

        public static class ViewHolder extends RecyclerView.ViewHolder {
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

            Picasso.with(viewHolder.mImageview.getContext())
                    .load(artist.mImageUrl)
                    .into(viewHolder.mImageview);


            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, artist.mSpotifyId).putExtra("Artist", artist.mName);
                    context.startActivity(intent);
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



