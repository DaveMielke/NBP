package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;

import org.nbp.common.Timeout;

import android.util.Log;

public abstract class Channel {
  private final static String LOG_TAG = Channel.class.getName();

  private final RemoteEndpoint remoteEndpoint;

  protected Channel (RemoteEndpoint endpoint) {
    remoteEndpoint = endpoint;
  }

  public abstract boolean write (byte b);
  public abstract boolean flush ();

  protected final static int BYTE_MASK = 0XFF;

  protected static void closeObject (Closeable object, String description) {
    try {
      object.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("%s close error: %s", description, exception.getMessage()), exception);
    }
  }

  protected final void resetInput (boolean readTimedOut) {
  }

  protected final boolean handleInput (byte b) {
    return remoteEndpoint.onByteReceived(b);
  }

  private Timeout readTimeout = new Timeout(ApplicationParameters.BLUETOOTH_READ_TIMEOUT, "braille-display-read-timeout") {
    @Override
    public void run () {
      synchronized (this) {
        Log.w(LOG_TAG, "bluetooth read timeout");
        resetInput(true);
      }
    }
  };

  protected final void handleInput (InputStream stream) {
    resetInput(false);

    while (true) {
      int b;

      try {
        b = stream.read();
      } catch (IOException exception) {
        Log.w(LOG_TAG, "bluetooth input error: " + exception.getMessage());
        break;
      }

      synchronized (readTimeout) {
        readTimeout.cancel();

        if (b == -1) {
          Log.w(LOG_TAG, "bluetooth end of input");
          break;
        }

        if (!handleInput((byte)(b & BYTE_MASK))) {
          readTimeout.start();
        } else if (!flush()) {
          break;
        }
      }
    }

    readTimeout.cancel();
  }
}
