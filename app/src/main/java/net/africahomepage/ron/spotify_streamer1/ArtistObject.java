package net.africahomepage.ron.spotify_streamer1;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ron on 07/07/15.
 */
public class ArtistObject implements Parcelable {
    public String mName;
    public String mSpotifyId;
    public String mImageUrl;
    public String mPreviewUrl;



    public ArtistObject(String name, String spotifyId, String imageUrl ) {
        this.mName = name;
        this.mSpotifyId = spotifyId;
        this.mImageUrl = imageUrl;

    }

    private ArtistObject(Parcel source) {
        mName =source.readString();
        mSpotifyId = source.readString();
        mImageUrl = source.readString();
    }

    public static final Parcelable.Creator<ArtistObject> CREATOR =
            new Parcelable.Creator<ArtistObject>() {

                @Override
                public ArtistObject[] newArray(int size) {
                    return new ArtistObject[size];
                }

                @Override
                public ArtistObject createFromParcel(Parcel source) {

                    return new ArtistObject(source);
                }
            };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mSpotifyId);
        dest.writeString(mImageUrl);
        String spotifyID = mSpotifyId;
        Bundle bundle = new Bundle();
        bundle.putString("SPOTIFYID", spotifyID);
        bundle.putString("Artist", mName);
        dest.writeBundle(bundle);
    }
}
