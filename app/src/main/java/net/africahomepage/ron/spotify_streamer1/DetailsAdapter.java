package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ron on 07/07/15.
 */
public class DetailsAdapter extends ArrayAdapter<TrackObject>{

    public DetailsAdapter(Context context, List<TrackObject> trackObjects) {
        super(context, 0, trackObjects );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackObject current  = getItem(position);

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.details_listview_layout, parent, false);

        ImageView imageview = (ImageView) rootView.findViewById(R.id.details_listview_layout_imageview);
        Picasso.with(getContext()).load(current.mTrackSmallImageUrl).into(imageview);

        TextView trackTitle = (TextView) rootView.findViewById(R.id.details_listview_layout_song);
        trackTitle.setText(current.mTrackTitle);

        TextView trackAlbum = (TextView) rootView.findViewById(R.id.details_listview_layout_album);
        trackAlbum.setText(current.mTrackAlbum);

        return  rootView;
    }

}

