package org.nbp.b2g.ui.display;
import org.nbp.b2g.ui.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;

import org.nbp.common.Timeout;

import android.util.Log;

public abstract class Channel extends Component implements Runnable {
  private final static String LOG_TAG = Channel.class.getName();

  protected Channel () {
    super();
  }

  protected final Protocol getProtocol () {
    return getEndpoint().getProtocol();
  }

  private Thread channelThread = null;

  protected abstract void initializeChannelThread ();
  protected abstract void runChannelThread ();
  protected abstract void stopChannelThread ();

  public abstract boolean send (byte b);
  public abstract boolean flush ();

  @Override
  public final void run () {
    Log.d(LOG_TAG, "channel thread starting");
    runChannelThread();
    Log.d(LOG_TAG, "channel thread stopped");
  }

  public final boolean start () {
    synchronized (this) {
      if (channelThread != null) return false;

      Log.d(LOG_TAG, "starting channel");
      channelThread = new Thread(this, "remote-display-channel");

      initializeChannelThread();
      channelThread.start();
      return true;
    }
  }

  public final boolean stop () {
    synchronized (this) {
      if (channelThread == null) return false;

      Log.d(LOG_TAG, "stopping channel");
      stopChannelThread();

      while (true) {
        try {
          channelThread.join();
          break;
        } catch (InterruptedException exception) {
        }
      }

      channelThread = null;
      return true;
    }
  }

  protected static void closeObject (Closeable object, String description) {
    try {
      object.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, String.format("%s close error: %s", description, exception.getMessage()), exception);
    }
  }

  protected final void resetInput () {
    getProtocol().resetInput();
  }

  protected final boolean handleTimeout () {
    return getProtocol().handleTimeout();
  }

  protected final boolean handleInput (byte b) {
    return getProtocol().handleInput(b);
  }

  private Timeout readTimeout = new Timeout(ApplicationParameters.DISPLAY_READ_TIMEOUT, "display-read-timeout") {
    @Override
    public void run () {
      synchronized (this) {
        Log.w(LOG_TAG, "channel read timeout");
        handleTimeout();
        resetInput();
      }
    }
  };

  protected final void handleInput (InputStream stream) {
    resetInput();

    while (true) {
      int b;

      try {
        b = stream.read();
      } catch (IOException exception) {
        Log.w(LOG_TAG, ("channel read error: " + exception.getMessage()));
        break;
      }

      synchronized (readTimeout) {
        readTimeout.cancel();

        if (b == -1) {
          Log.w(LOG_TAG, "channel end of input");
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
