package net.africahomepage.ron.spotify_streamer1;

/**
 * Created by ron on 11/07/15.
 */
public class TrackObject  {
    public String mTrackAlbum;
    public String mTrackTitle;
    public String mTrackSmallImageUrl;
    public String mTrackLargeImageUrl;
    public String mPreviewUrl;

    public TrackObject(String trackAlbum, String trackTitle, String trackSmallImageUrl, String trackLargeImageUrl, String previewUrl ) {
        this. mTrackAlbum = trackAlbum;
        this.mTrackTitle = trackTitle;
        this.mTrackSmallImageUrl = trackSmallImageUrl;
        this.mTrackLargeImageUrl= trackLargeImageUrl;
        this.mPreviewUrl = previewUrl;
    }

}
