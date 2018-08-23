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

  private final static Queue<Uri> uriQueue = new LinkedList<Uri>();
  private static boolean isActive = false;

  private static void playNext (MediaPlayer player, boolean reset) {
    synchronized (uriQueue) {
      while (true) {
        Uri uri = uriQueue.poll();
        if (uri == null) break;
        if (reset) player.reset();

        try {
          player.setDataSource(getContext(), uri);
          player.prepareAsync();
          return;
        } catch (IOException exception) {
          Log.e(LOG_TAG, ("set data source error: " + exception.getMessage()));
        }
      }

      player.release();
      isActive = false;
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
          playNext(player, true);
          return true;
        }
      }
    );
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
          playNext(player, true);
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
      if (uri != null) uriQueue.offer(uri);

      if (!isActive) {
        if (withAttentionSignal) {
          MediaPlayer player = MediaPlayer.create(getContext(), R.raw.attention_signal);
          setListeners(player);
          player.start();
        } else {
          if (uriQueue.isEmpty()) return;

          MediaPlayer player = new MediaPlayer();
          setListeners(player);
          playNext(player, false);
        }

        isActive = true;
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
