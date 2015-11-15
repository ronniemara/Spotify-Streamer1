package net.africahomepage.ron.spotify_streamer1;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer;

class MediaPlayerOnPreparedListener implements OnPreparedListener {

   @Override
  public void onPrepared(MediaPlayer mp) {
    mp.start();
  }
}
