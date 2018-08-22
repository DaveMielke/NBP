package org.nbp.ipaws;

import android.util.Log;

import android.media.MediaPlayer;
import android.net.Uri;
import java.io.File;
import java.io.IOException;

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
          player.release();
        }
      }
    );
  }

  private static void setListeners (MediaPlayer player) {
    setOnErrorListener(player);
    setOnPreparedListener(player);
    setOnCompletionListener(player);
  }

  public static void play (Uri uri) {
    MediaPlayer player = new MediaPlayer();
    setListeners(player);

    try {
      player.setDataSource(getContext(), uri);
    } catch (IOException exception) {
      Log.e(LOG_TAG, ("set data source error: " + exception.getMessage()));
      return;
    }

    player.prepareAsync();
  }

  public static void play (String url) {
    play(Uri.parse(url));
  }

  public static void play (File file) {
    play(Uri.fromFile(file));
  }
}
