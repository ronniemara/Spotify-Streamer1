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
public class ArtistAdapter extends ArrayAdapter<ArtistObject>{

    public ArtistAdapter(Context context, List<ArtistObject> artistObjects) {
        super(context, 0, artistObjects );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistObject current  = getItem(position);

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.main_listview_textview, parent, false);

        ImageView imageview = (ImageView) rootView.findViewById(R.id.artist_imageview);
        Picasso.with(getContext()).load(current.mImageUrl).into(imageview);

        TextView artistNAme = (TextView) rootView.findViewById(R.id.artist_name_textview);
        artistNAme.setText(current.mName);

        return  rootView;
    }

}

