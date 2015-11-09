package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by ron on 07/07/15.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.TrackViewHolder>{

    ArrayList<TrackObject> mDataSet;

    public DetailsAdapter(ArrayList<TrackObject> item) {
        mDataSet = item;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_recycler_view_layout, parent, false);

        TrackViewHolder viewHolder = new TrackViewHolder(rootView);



        return  viewHolder;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {

        final TrackObject item = mDataSet.get(position);

        Picasso.with(holder.container.getContext())
                .load(item.mTrackSmallImageUrl)
                .into(holder.imageView);

        holder.trackTitle.setText(item.mTrackTitle);

        holder.trackAlbum.setText(item.mTrackAlbum);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Context context = v.getContext();
                Intent intent = new Intent(context, SongPlayerActivity.class);
                intent.putExtra("net.africahomepage.ron.Track", item);
                context.startActivity(intent);

            }
        });
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        protected View container;
        protected ImageView imageView;
        protected TextView trackTitle;
        protected TextView trackAlbum;

        public TrackViewHolder(View itemView) {
            super(itemView);

            container = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.details_listview_layout_imageview);
            trackTitle = (TextView) itemView.findViewById(R.id.details_listview_layout_song);
            trackAlbum = (TextView) itemView.findViewById(R.id.details_listview_layout_album);
        }
    }

}

