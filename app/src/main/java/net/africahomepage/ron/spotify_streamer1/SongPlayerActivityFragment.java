package net.africahomepage.ron.spotify_streamer1;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by ron on 08/11/15.
 */
public class SongPlayerActivityFragment extends Fragment{

    TrackObject mTrack= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if(extras.containsKey("net.africahomepage.ron.Track")) {
            mTrack = extras.getParcelable("net.africahomepage.ron.Track");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_player, container);
        ImageView imageView = (ImageView) view.findViewById(R.id.song_player_image_view);
        Picasso.with(getActivity()).load(mTrack.mTrackLargeImageUrl).into(imageView);

        return view;
    }


}
