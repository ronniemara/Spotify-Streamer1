package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ron on 07/07/15.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.ViewHolder> {

    private List<TrackObject> mTrackData;
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground=0;


    public TracksAdapter(Context context, List<TrackObject> trackObjects) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mTrackData = trackObjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_listview_layout, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

