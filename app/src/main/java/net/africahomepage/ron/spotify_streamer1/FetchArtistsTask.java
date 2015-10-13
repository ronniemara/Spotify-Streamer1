package net.africahomepage.ron.spotify_streamer1;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by ron on 08/10/15.
 */
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
