package net.africahomepage.ron.spotify_streamer1;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.africahomepage.ron.spotify_streamer1.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
