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
 * Created by ron on 08/10/15.
 */
class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private List<ArtistObject> mValues;
    private int mBackground;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mArtistNAme;
        public final ImageView mImageview;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mArtistNAme = (TextView) view.findViewById(R.id.artist_name_textview);
            mImageview = (ImageView) view.findViewById(R.id.artist_imageview);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_listview_textview, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final ArtistObject artist = mValues.get(i);

        viewHolder.mArtistNAme.setText(artist.mName);
        Picasso.with(viewHolder.mImageview.getContext())
                .load(artist.mImageUrl)
                .into(viewHolder.mImageview);


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("net.africahomepage.ron.spotify_streamer1.artist", artist);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public ArtistsAdapter(Context context, List<ArtistObject> artistObjects) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = artistObjects;
    }

}
