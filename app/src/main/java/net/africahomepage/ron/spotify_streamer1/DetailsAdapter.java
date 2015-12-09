package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.policy.Resource;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by ron on 07/07/15.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.TrackViewHolder>{

    ArrayList<TrackObject> mDataSet;
    String mArtist;

    public DetailsAdapter(String artist, ArrayList<TrackObject> item) {
        mArtist = artist; 
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
    public void onBindViewHolder(TrackViewHolder holder, final int position) {

        final TrackObject item = mDataSet.get(position);

        Picasso.with(holder.container.getContext())
                .load(item.mTrackSmallImageUrl)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.trackTitle.setText(item.mTrackTitle);

        holder.trackAlbum.setText(item.mTrackAlbum);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               View details = v.getRootView().findViewById(R.id.details);
                boolean dualPane = details != null && details.getVisibility() == View.VISIBLE;

                if(!dualPane) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putParcelableArrayListExtra("net.africahomepage.ron.Tracks", mDataSet);
                    intent.putExtra("net.africahomepage.ron.index", position);
                    intent.putExtra("net.africahomepage.ron.artist", mArtist);
                    context.startActivity(intent);
                } else {
                    FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    PlayerFragDialog playerFrag =  PlayerFragDialog
                            .newInstance((AppCompatActivity)v.getContext());
                    Bundle args = new Bundle();
                    args.putString("net.africahomepage.ron.artist", mArtist);
                    args.putInt("net.africahomepage.ron.index", position);

                    playerFrag.setArguments(args);

                    playerFrag.show(manager, "dialog");
                    ft.commit();


                }

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

