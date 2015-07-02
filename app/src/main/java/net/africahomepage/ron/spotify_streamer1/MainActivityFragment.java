package net.africahomepage.ron.spotify_streamer1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayAdapter<String> mSpotifyadapter = null;
    ArtistsPager mArtistData = null;


    public MainActivityFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        EditText searchEditText = (EditText) root.findViewById(R.id.search_editText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = v.getText().toString();
                    FetchMusicTask fetchMusicTask = new FetchMusicTask();
                    fetchMusicTask.execute(searchText);
                    handled = true;
                }
                return handled;
            }
        });
        String[] artists = {
                "Beyonce",
                "Gucci Mane",
                "Mariah Carey"
        };



        ArrayList<String> data = new ArrayList<>(
                Arrays.asList(artists)
        );

        mSpotifyadapter = new ArrayAdapter<String>(getActivity(),R.layout.main_listview_textview,R.id.listview_textview,data);

        ListView listView = (ListView) root.findViewById(R.id.mainActivity_listView);
        listView.setAdapter(mSpotifyadapter);

        return root;
    }

    public class FetchMusicTask extends AsyncTask<String, Void, ArtistsPager> {

        @Override
        protected void onPostExecute(ArtistsPager result) {
           mArtistData = result;
        }

        @Override
        protected ArtistsPager doInBackground(String... artist) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager artistData = spotify.searchArtists(artist[0]);
            return artistData;
        }

    }
}
