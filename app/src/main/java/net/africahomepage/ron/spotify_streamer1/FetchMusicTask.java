package net.africahomepage.ron.spotify_streamer1;

import android.os.AsyncTask;

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
class FetchMusicTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG =  FetchMusicTask.class.getSimpleName();
    public OnTaskCompletedListiner listener = null;
    private ArtistAdapter adapter = null;
    private ArrayList<ArtistObject> mArtists = null;



    public FetchMusicTask(OnTaskCompletedListiner listener, ArtistAdapter adapter, ArrayList<ArtistObject> artists) {
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
        ArtistsPager artistsData = spotify.searchArtists(artist[0]);
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
