package net.africahomepage.ron.spotify_streamer1;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer;
import android.widget.MediaController;

class PlayerOnPreparedListener implements OnPreparedListener {

   @Override
  public void onPrepared(MediaPlayer mp) {
   MediaController.MediaPlayerControl mpc = MyMediaPlayer.newInstance(mp);
       mpc.start();
  }
}
