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

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnArtistClickListener,
PlayerFragDialog.onControlMediaPlayer{

    @Override
    public void passMediaPlayer(AppCompatActivity activity, MediaPlayer mp, int index) {

    }

    boolean mDualPane =false;
    ArtistObject mArtistObject = null;
    MainFragment mMainFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //check to see if dual pane mode
        View details = findViewById(R.id.details);
        mDualPane = details != null && details.getVisibility() == View.VISIBLE;

        mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mMainFragment.setOnArtistClickListener(this);


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

                ft.replace(R.id.details, detailsFragment, "details");
            } else {
                detailsFragment = new DetailsFragment();
                ft.replace(R.id.details, detailsFragment, "details");
            }

            ft.commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("artist", artist);
            startActivity(intent);
        }
    }
}
