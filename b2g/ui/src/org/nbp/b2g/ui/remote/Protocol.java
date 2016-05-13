package org.nbp.b2g.ui.remote;
import org.nbp.b2g.ui.*;

import android.util.Log;

public abstract class Protocol {
  private final static String LOG_TAG = Protocol.class.getName();

  private final RemoteEndpoint remoteEndpoint;

  protected Protocol (RemoteEndpoint endpoint) {
    remoteEndpoint = endpoint;
  }

  protected static void logIgnoredByte (byte b) {
    Log.w(LOG_TAG, String.format("input byte ignored: 0X%02X", b));
  }

  public void resetInput (boolean timeout) {
  }

  public boolean handleInput (byte b) {
    logIgnoredByte(b);
    return true;
  }
}
