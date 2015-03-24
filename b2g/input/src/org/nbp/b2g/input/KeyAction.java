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
    name = (name != null)? (" (" + name + ")"): "";

    Log.d(LOG_TAG, String.format("sending %s code %s: %d%s",
      type, (press? "press": "release"), value, name
    ));
  }

  protected abstract class KeyCombinationSender {
    protected abstract boolean sendKeyPress (int key);
    protected abstract boolean sendKeyRelease (int key);

    public final boolean sendKeyCombination (int key, int[] modifiers) {
      for (int modifier : modifiers) {
        if (!sendKeyPress(modifier)) return false;
      }

      if (!sendKeyPress(key)) return false;
      waitForHoldTime();
      if (!sendKeyRelease(key)) return false;

      for (int modifier : modifiers) {
        if (!sendKeyRelease(modifier)) return false;
      }

      return true;
    }

    public final boolean sendKeyCombination (int key) {
      return sendKeyCombination(key, null);
    }
  }

  protected KeyAction () {
    super();
  }
}
