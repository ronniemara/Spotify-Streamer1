package net.africahomepage.ron.spotify_streamer1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnArtistClickListener,
        DetailsFragment.onTrackClickListener
{

    static final String ARTIST_DATA = "artistData";


    boolean mDualPane =false;
    ArtistObject mArtistObject = null;
    MainFragment mMainFragment = null;

    DetailsFragment mDetailsFragment =  null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDualPane = getResources().getBoolean(R.bool.large_layout);

        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mMainFragment.setOnArtistClickListener(this);

        mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_details);
    if(mDetailsFragment !=null) {
        mDetailsFragment.setOnTrackClickListener(this);
    }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUI(ArtistObject artist) {
       mArtistObject = artist;

        if(mDualPane) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            DetailsFragment detailsFragment = DetailsFragment.newInstance(artist);

            if(detailsFragment !=null) {
                detailsFragment.setOnTrackClickListener(this);
                ft.replace(R.id.details, detailsFragment, "details");
            } else {
                detailsFragment = new DetailsFragment();
                ft.replace(R.id.details, detailsFragment, "details");
            }
            ft.commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(ARTIST_DATA, artist);
            startActivity(intent);
        }
    }


    public void launchDialog(int position, ArrayList<TrackObject> mDataSet, String mArtist) {

        boolean dualPane = this.getResources().getBoolean(R.bool.large_layout);

        if(!dualPane) {
            Intent intent = new Intent(this, PlayerActivity.class);
            intent.putParcelableArrayListExtra("net.africahomepage.ron.Tracks", mDataSet);
            intent.putExtra("net.africahomepage.ron.index", position);
            intent.putExtra("net.africahomepage.ron.artist", mArtist);
            startActivity(intent);
        } else {
android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


            //check if fragment already exists
            PlayerFragDialog prev = (PlayerFragDialog) getSupportFragmentManager().findFragmentByTag("dialog");
            if(prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            PlayerFragDialog playerFrag =  PlayerFragDialog
                    .newInstance(this);
            Bundle args = new Bundle();
            args.putParcelableArrayList("net.africahomepage.ron.Tracks", mDataSet);
            args.putString("net.africahomepage.ron.artist", mArtist);
            args.putInt("net.africahomepage.ron.index", position);

            playerFrag.setArguments(args);

            playerFrag.show(getSupportFragmentManager(), "dialog");
            ft.commit();


        }

    }
}
