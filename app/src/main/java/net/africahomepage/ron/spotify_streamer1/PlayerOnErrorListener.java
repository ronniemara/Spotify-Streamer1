package net.africahomepage.ron.spotify_streamer1;

import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer;
import android.util.Log;

class PlayerOnErrorListener implements OnErrorListener {



  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    Log.e(mp.toString(), "Error" + what + extra);
    return false;
  }
}
