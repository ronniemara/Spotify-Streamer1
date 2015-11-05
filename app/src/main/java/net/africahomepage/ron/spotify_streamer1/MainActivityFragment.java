package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.SearchView;
import android.widget.ImageView;
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



    public ArtistsAdapter mSpotifyadapter = null;
    public ArrayList<ArtistObject> mArtistData = new ArrayList<>();


    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("ArtistDAta", mArtistData);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                OnTaskCompletedListener listiner = new OnTaskCompletedListener() {
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
                    FetchArtistsTask fetchArtistsTask = new FetchArtistsTask(listiner, mSpotifyadapter, mArtistData);
                    fetchArtistsTask.execute(query);

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey("ArtistDAta")) {
            mArtistData = new ArrayList<>();
        } else {
            mArtistData = savedInstanceState.getParcelableArrayList("ArtistDAta");

        }

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        // Get the intent, verify the action and get the query

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mSpotifyadapter = new ArtistsAdapter(getActivity(), mArtistData);

        RecyclerView recycView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recycView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycView.setAdapter(mSpotifyadapter);

        return root;
    }
    class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private List<ArtistObject> mValues;
        private int mBackground;

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
            Picasso.with(viewHolder.mImageview.getContext())
                    .load(artist.mImageUrl)
                    .into(viewHolder.mImageview);


            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsActivity.class);
                    // Bundle bundle = new Bundle();
                    intent.putExtra("net.africahomepage.ron.spotify_streamer1.artist", artist);

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public ArtistsAdapter(Context context, List<ArtistObject> artistObjects) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = artistObjects;
        }

    }

    public class FetchArtistsTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG =  FetchArtistsTask.class.getSimpleName();
        public OnTaskCompletedListener listener = null;
         private ArtistsAdapter adapter = null;
        private ArrayList<ArtistObject> mArtists = null;



        public FetchArtistsTask(OnTaskCompletedListener listener, ArtistsAdapter adapter, ArrayList<ArtistObject> artists) {
            this.listener = listener;
            this.adapter = adapter;
            this.mArtists = artists;
        }

        protected void onPostExecute(Void result) {

            listener.taskCompleted();

            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... artist) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager artistsData = null;
            try {
                artistsData = spotify.searchArtists(artist[0]);

            } catch(Exception e) {
                Log.e(LOG_TAG, e.getMessage().toString());

            }
            List<Artist> artistInfo = artistsData.artists.items;

            mArtists.clear();

            if (artistInfo.isEmpty()) {
                return null;
            }

            for (int i = 0; i < artistInfo.size(); i++) {
                Artist art = artistInfo.get(i);
                List<Image> images = art.images;
                mArtists.add(new ArtistObject(
                        art.name,
                        art.id,
                        images.isEmpty() ? null : images.get(0).url
                ));
            }

            return null;
        }
    }



}



