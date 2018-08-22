package org.nbp.ipaws;

import android.util.Log;

import android.media.MediaPlayer;
import android.net.Uri;
import java.io.File;
import java.io.IOException;

import java.util.Queue;
import java.util.LinkedList;

public abstract class AlertPlayer extends ApplicationComponent {
  private final static String LOG_TAG = AlertPlayer.class.getName();

  private AlertPlayer () {
  }

  private final static String getErrorMessage (int error) {
    switch (error) {
      case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
        return "media player died";

      default:
        return null;
    }
  }

  private static void setOnErrorListener (MediaPlayer player) {
    player.setOnErrorListener(
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
          return false;
        }
      }
    );
  }

  private final static Queue<Uri> uriQueue = new LinkedList<Uri>();

  private static void playNextUri (MediaPlayer player) {
    synchronized (uriQueue) {
      Uri uri = uriQueue.poll();

      if (uri != null) {
        try {
          player.setDataSource(getContext(), uri);
          player.prepareAsync();
          return;
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("set data source error: " + exception.getMessage()));
        }
      }
    }

    player.release();
  }

  private static void setOnPreparedListener (MediaPlayer player) {
    player.setOnPreparedListener(
      new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared (MediaPlayer player) {
          player.start();
        }
      }
    );
  }

  private static void setOnCompletionListener (MediaPlayer player) {
    player.setOnCompletionListener(
      new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion (MediaPlayer player) {
          player.reset();
          playNextUri(player);
        }
      }
    );
  }

  private static void setListeners (MediaPlayer player) {
    setOnErrorListener(player);
    setOnPreparedListener(player);
    setOnCompletionListener(player);
  }

  public static void play (Uri uri, boolean withAttentionSignal) {
    synchronized (uriQueue) {
      uriQueue.offer(uri);

      if (withAttentionSignal) {
        MediaPlayer player = MediaPlayer.create(getContext(), R.raw.attention_signal);
        setListeners(player);
        player.start();
      } else {
        MediaPlayer player = new MediaPlayer();
        setListeners(player);
        playNextUri(player);
      }
    }
  }

  public static void play (String url, boolean withAttentionSignal) {
    play(Uri.parse(url), withAttentionSignal);
  }

  public static void play (File file, boolean withAttentionSignal) {
    play(Uri.fromFile(file), withAttentionSignal);
  }
}
