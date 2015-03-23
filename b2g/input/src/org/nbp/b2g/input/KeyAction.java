package org.nbp.b2g.input;

import android.util.Log;

public abstract class KeyAction extends Action {
  private static final String LOG_TAG = KeyAction.class.getName();

  protected long getHoldTime () {
    return 0;
  }

  protected void waitForHoldTime () {
    long holdTime = getHoldTime();

    if (holdTime > 0) ApplicationUtilities.sleep(holdTime + ApplicationParameters.LONG_PRESS_DELAY);
  }

  protected void logKeyEvent (String type, String name, int value, boolean press) {
    Log.d(LOG_TAG, String.format("sending %s code %s: %d (%s)",
      type, (press? "press": "release"), value, name
    ));
  }

  protected KeyAction () {
    super();
  }
}
