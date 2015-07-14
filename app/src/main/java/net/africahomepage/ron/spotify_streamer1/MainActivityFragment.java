package net.africahomepage.ron.spotify_streamer1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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
            mArtistData = new ArrayList<ArtistObject>();
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
                FetchMusicTask fetchMusicTask = new FetchMusicTask();
                fetchMusicTask.execute(query);
                if(mArtistData.isEmpty()) {
                    Toast.makeText(getActivity(), "There is no artists information for your query", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        mSpotifyadapter = new ArtistAdapter(getActivity(),mArtistData);

        ListView listView = (ListView) root.findViewById(R.id.mainActivity_listView);
        listView.setAdapter(mSpotifyadapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistObject item = mSpotifyadapter.getItem(position);
                Intent startDetails = new Intent(getActivity(), DetailsActivity.class).putExtra(Intent.EXTRA_TEXT, item.mSpotifyId);
                startActivity(startDetails);

            }
        });

        return root;
    }

        public class FetchMusicTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG =  FetchMusicTask.class.getSimpleName();

        protected void onPostExecute(Void result) {

            mSpotifyadapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... artist) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager artistsData = spotify.searchArtists(artist[0]);
            List<Artist> artistInfo = artistsData.artists.items;
            if (artistInfo.isEmpty()) {
                return null;
            }
            mArtistData.clear();

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
}
