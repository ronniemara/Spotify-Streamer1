package net.africahomepage.ron.spotify_streamer1;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DetailsActivity extends AppCompatActivity
        implements PlayerFragDialog.onControlMediaPlayer {

    DetailsFragment mDetailsFragement;
    PlayerFragDialog mPlayerFragDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        mDetailsFragement = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_details);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

    @Override
    public void passMediaPlayer(AppCompatActivity activity, MediaPlayer mp, int index) {
        new Util().passMediaPlayer(activity,mp,index);
    }
}
