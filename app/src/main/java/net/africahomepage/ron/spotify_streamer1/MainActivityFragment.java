package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

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


}



