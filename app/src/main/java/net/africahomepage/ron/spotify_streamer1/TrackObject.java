package net.africahomepage.ron.spotify_streamer1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ron on 11/07/15.
 */
public class TrackObject implements Parcelable {
    public String mTrackAlbum;
    public String mTrackTitle;
    public String mTrackSmallImageUrl;
    public String mTrackLargeImageUrl;
    public String mPreviewUrl;

    public TrackObject(String trackAlbum, String trackTitle, String trackSmallImageUrl, String trackLargeImageUrl, String previewUrl ) {
        mTrackAlbum = trackAlbum;
        mTrackTitle = trackTitle;
        mTrackSmallImageUrl = trackSmallImageUrl;
        mTrackLargeImageUrl= trackLargeImageUrl;
        mPreviewUrl = previewUrl;
    }

    private TrackObject(Parcel in) {
        mTrackAlbum = in.readString();
        mTrackTitle = in.readString();
        mTrackSmallImageUrl = in.readString();
        mTrackLargeImageUrl = in.readString();
        mPreviewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrackAlbum);
        dest.writeString(mTrackTitle);
        dest.writeString(mTrackSmallImageUrl);
        dest.writeString(mTrackLargeImageUrl);
        dest.writeString(mPreviewUrl);
    }

    public static final Parcelable.Creator<TrackObject> CREATOR
            = new Parcelable.ClassLoaderCreator<TrackObject>() {

        @Override
        public TrackObject createFromParcel(Parcel source) {
            return new TrackObject(source);
        }

        @Override
        public TrackObject[] newArray(int size) {
            return new TrackObject[0];
        }

        @Override
        public TrackObject createFromParcel(Parcel source, ClassLoader loader) {
            return new TrackObject(source);
        }
    };
}
