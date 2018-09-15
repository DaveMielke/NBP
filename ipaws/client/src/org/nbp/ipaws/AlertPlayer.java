package org.nbp.ipaws;

import android.util.Log;

import org.nbp.common.CommonUtilities;

import android.media.MediaPlayer;
import android.media.AudioAttributes;

import android.net.Uri;
import java.io.File;
import java.io.IOException;

import java.util.Queue;
import java.util.LinkedList;

public abstract class AlertPlayer extends ApplicationComponent {
  private final static String LOG_TAG = AlertPlayer.class.getName();

  private AlertPlayer () {
    super();
  }

  private final static Queue<Uri> uriQueue = new LinkedList<Uri>();
  private static MediaPlayer mediaPlayer = null;

  private static String uriToString (Uri uri) {
    return Uri.decode(uri.toString());
  }

  private static void setAudioAttributes () {
    AudioAttributes.Builder builder = new AudioAttributes.Builder();
    builder.setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED);
    builder.setUsage(AudioAttributes.USAGE_NOTIFICATION);
    builder.setContentType(AudioAttributes.CONTENT_TYPE_SPEECH);
    mediaPlayer.setAudioAttributes(builder.build());
  }

  private static void playNext (boolean reset) {
    synchronized (uriQueue) {
      while (true) {
        Uri uri = uriQueue.poll();
        if (uri == null) break;
        if (reset) mediaPlayer.reset();
        Log.d(LOG_TAG, ("playing alert: " + uriToString(uri)));

        try {
          if (CommonUtilities.haveLollipop) setAudioAttributes();
          mediaPlayer.setDataSource(getContext(), uri);
          mediaPlayer.prepareAsync();
          return;
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("set data source error: " + exception.getMessage()));
        }
      }

      mediaPlayer.release();
      mediaPlayer = null;
    }
  }

  private final static String getErrorMessage (int error) {
    switch (error) {
      case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
        return "media player died";

      default:
        return null;
    }
  }

  private static void setOnErrorListener () {
    mediaPlayer.setOnErrorListener(
      new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError (MediaPlayer player, int error, int extra) {
          StringBuilder log = new StringBuilder();
          log.append("media player error ");
          log.append(error);
          log.append('.');
          log.append(extra);

          {
            String message = getErrorMessage(error);

            if (message != null) {
              log.append(": ");
              log.append(message);
            }
          }

          Log.e(LOG_TAG, log.toString());
          playNext(true);
          return true;
        }
      }
    );
  }

  private static void setOnPreparedListener () {
    mediaPlayer.setOnPreparedListener(
      new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared (MediaPlayer player) {
          mediaPlayer.start();
        }
      }
    );
  }

  private static void setOnCompletionListener () {
    mediaPlayer.setOnCompletionListener(
      new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion (MediaPlayer player) {
          playNext(true);
        }
      }
    );
  }

  private static void configurePlayer () {
    setOnErrorListener();
    setOnPreparedListener();
    setOnCompletionListener();
  }

  public static void play (Uri uri, boolean withAttentionSignal) {
    synchronized (uriQueue) {
      if (uri != null) {
        Log.d(LOG_TAG, ("enqueuing alert: " + uriToString(uri)));
        uriQueue.offer(uri);
      }

      if (mediaPlayer == null) {
        if (withAttentionSignal) {
          mediaPlayer = MediaPlayer.create(getContext(), R.raw.attention_signal);
          configurePlayer();
          mediaPlayer.start();
        } else {
          if (uriQueue.isEmpty()) return;

          mediaPlayer = new MediaPlayer();
          configurePlayer();
          playNext(false);
        }
      }
    }
  }

  public static void play (String url, boolean withAttentionSignal) {
    Uri uri = null;
    if (url != null) uri = Uri.parse(url);
    play(uri, withAttentionSignal);
  }

  public static void play (File file, boolean withAttentionSignal) {
    Uri uri = null;
    if (file != null) uri = Uri.fromFile(file);
    play(uri, withAttentionSignal);
  }

  public static void play () {
    play((Uri)null, true);
  }

  public static void stop () {
    synchronized (uriQueue) {
      uriQueue.clear();

      if (mediaPlayer != null) {
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();

        mediaPlayer.setOnPreparedListener(null);
        mediaPlayer.setOnCompletionListener(null);

        mediaPlayer.release();
        mediaPlayer = null;
      }
    }
  }
}
