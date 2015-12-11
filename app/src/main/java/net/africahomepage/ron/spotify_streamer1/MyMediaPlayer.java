package net.africahomepage.ron.spotify_streamer1;

import android.media.MediaPlayer;
import android.widget.MediaController;

/**
 * Created by ron on 09/12/15.
 */
public class MyMediaPlayer implements MediaController.MediaPlayerControl{

    private MediaPlayer mMediaPlayer;

    public static MyMediaPlayer newInstance(MediaPlayer mp) {
        MyMediaPlayer m = new MyMediaPlayer();
        m.mMediaPlayer = mp;

        return m;
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
