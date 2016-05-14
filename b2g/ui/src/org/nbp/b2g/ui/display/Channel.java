package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;

import org.nbp.common.Timeout;

import android.util.Log;

public abstract class Channel extends Component implements Runnable {
  private final static String LOG_TAG = Channel.class.getName();

  private Thread channelThread = null;

  protected Channel (DisplayEndpoint endpoint) {
    super(endpoint);
  }

  public final boolean start () {
    synchronized (this) {
      if (channelThread != null) return false;
      Log.d(LOG_TAG, "starting channel");
      channelThread = new Thread(this, "braille-display-channel");
      channelThread.start();
      return true;
    }
  }

  public final boolean stop () {
    synchronized (this) {
      if (channelThread == null) return false;
      Log.d(LOG_TAG, "stopping channel");
      channelThread.interrupt();
      channelThread = null;
      return true;
    }
  }

  public abstract boolean send (byte b);
  public abstract boolean flush ();

  protected static void closeObject (Closeable object, String description) {
    try {
      object.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("%s close error: %s", description, exception.getMessage()), exception);
    }
  }

  protected final void resetInput () {
    displayEndpoint.getProtocol().resetInput();
  }

  protected final boolean handleTimeout () {
    return displayEndpoint.getProtocol().handleTimeout();
  }

  protected final boolean handleInput (byte b) {
    return displayEndpoint.getProtocol().handleInput(b);
  }

  private Timeout readTimeout = new Timeout(ApplicationParameters.DISPLAY_READ_TIMEOUT, "display-read-timeout") {
    @Override
    public void run () {
      synchronized (this) {
        Log.w(LOG_TAG, "display read timeout");
        handleTimeout();
        resetInput();
      }
    }
  };

  protected final void handleInput (InputStream stream) {
    resetInput();

    while (!Thread.interrupted()) {
      int b;

      try {
        b = stream.read();
      } catch (IOException exception) {
        Log.w(LOG_TAG, "display input error: " + exception.getMessage());
        break;
      }

      synchronized (readTimeout) {
        readTimeout.cancel();

        if (b == -1) {
          Log.w(LOG_TAG, "display end of input");
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
