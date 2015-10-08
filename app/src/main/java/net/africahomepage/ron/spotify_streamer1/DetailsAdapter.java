package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    List<TrackObject> mTrackData;


    public DetailsAdapter(Context context, List<TrackObject> trackObjects) {
        mTrackData = trackObjects;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TrackObject current  = getItem(position);
//
//        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.details_listview_layout, parent, false);
//
//        ImageView imageview = (ImageView) rootView.findViewById(R.id.details_listview_layout_imageview);
//        Picasso.with(getContext()).load(current.mTrackSmallImageUrl).into(imageview);
//
//        TextView trackTitle = (TextView) rootView.findViewById(R.id.details_listview_layout_song);
//        trackTitle.setText(current.mTrackTitle);
//
//        TextView trackAlbum = (TextView) rootView.findViewById(R.id.details_listview_layout_album);
//        trackAlbum.setText(current.mTrackAlbum);
//
//        return  rootView;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_listview_layout,parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TrackObject track = mTrackData.get(position);
        holder.mTrackTitle.setText(track.mTrackTitle);

        Picasso.with(holder.mImageview.getContext())
                .load(track.mTrackSmallImageUrl)
                .into(holder.mImageview);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SongPlayerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("net.africahomepage.ron.spotify_streamer1.track", track);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageView mImageview;
        TextView mTrackTitle;
        TextView mTrackAlbum;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mImageview = (ImageView) view.findViewById(R.id.details_listview_layout_imageview);
            mTrackTitle = (TextView) view.findViewById(R.id.details_listview_layout_song);
            mTrackAlbum = (TextView) view.findViewById(R.id.details_listview_layout_album);

                                }
    }
}

