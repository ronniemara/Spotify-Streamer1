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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.support.v7.widget.SearchView;
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

    public ArtistAdapter mSpotifyadapter = null;
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
                    FetchMusicTask fetchMusicTask = new FetchMusicTask(listiner, mSpotifyadapter, mArtistData);
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null || !savedInstanceState.containsKey("ArtistDAta")) {
            mArtistData = new ArrayList<>();
        }
        else {
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

        mSpotifyadapter = new ArtistAdapter(getActivity(), mArtistData);

        RecyclerView recycView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        recycView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycView.setAdapter(mSpotifyadapter);

        return root;
    }








    }



