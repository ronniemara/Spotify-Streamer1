package net.africahomepage.ron.spotify_streamer1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainFragment.OnUpdateUIListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Display display = getWindowManager().getDefaultDisplay();
        Point size = getDisplaySize(display);


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

    private static Point getDisplaySize(final Display display) {
        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    @Override
    public void updateUI(ArtistObject artist) {
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager().
                findFragmentById(R.id.tracks_views);
        if (fragment==null || ! fragment.isInLayout()) {
            // start new Activity
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, artist.mSpotifyId).putExtra("Artist", artist.mName);
            this.startActivity(intent);
        }
        else {
            fragment.update();
        }



    }
}
