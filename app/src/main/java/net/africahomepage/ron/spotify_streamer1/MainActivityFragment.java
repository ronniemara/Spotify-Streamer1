package net.africahomepage.ron.spotify_streamer1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSpotifyadapter = null;
    List<Artist> mArtistData = null;


    public MainActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        TextView searchEditText = (TextView) root.findViewById(R.id.search_editText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH ) {
                    String searchText = v.getText().toString();
                    FetchMusicTask fetchMusicTask = new FetchMusicTask();
                    fetchMusicTask.execute(searchText);
                    handled = true;
                }
                return handled;
            }
        });
//        String[] artists = {
//                "Beyonce",
//                "Gucci Mane",
//                "Mariah Carey"
//        };



//        ArrayList<String> data = new ArrayList<>(
//                Arrays.asList(artists)
//        );

        if(mArtistData != null) {

        }

        mSpotifyadapter = new ArrayAdapter<>(getActivity(),R.layout.main_listview_textview, R.id.artist_name_textview ,data);

        ListView listView = (ListView) root.findViewById(R.id.mainActivity_listView);
        listView.setAdapter(mSpotifyadapter);

        return root;
    }

    public class FetchMusicTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG =  FetchMusicTask.class.getSimpleName();
        protected void onPostExecute(List<Artist> result) {
           mArtistData = result;
        }

        @Override
        protected List<Artist> doInBackground(String... artist) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager artistData = spotify.searchArtists(artist[0]);
            List<Artist> artistList = artistData.artists.items;
            ArrayList<String> result = null;
            for (int i=0; i < artistList.size(); i++) {
                Artist art = artistList.get(i);

//                Log.v(LOG_TAG, art.name);
                try {
                    result.add(art.name);
                } catch (Exception e) {

                }
            }
            return artistList;
        }

    }
}
