package org.nbp.b2g.ui;

import android.util.Log;

public abstract class KeyAction extends Action {
  private final static String LOG_TAG = KeyAction.class.getName();

  protected long getHoldTime () {
    return 0;
  }

  protected void waitForHoldTime () {
    long holdTime = getHoldTime();

    if (holdTime > 0) ApplicationUtilities.sleep(holdTime + ApplicationParameters.LONG_PRESS_DELAY);
  }

  protected void logKeyEvent (String type, String name, int value, boolean press) {
    name = (name != null)? (" (" + name + ")"): "";

    Log.d(LOG_TAG, String.format(
      "sending %s %s: %d%s",
      type, (press? "press": "release"), value, name
    ));
  }

  protected abstract class KeyCombinationSender {
    protected abstract boolean sendKeyPress (int key);
    protected abstract boolean sendKeyRelease (int key);
    protected abstract String getKeyType ();

    protected String getKeyName (int key) {
      return null;
    }

    private boolean sendKeyEvent (int key, boolean press) {
      if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
        logKeyEvent(getKeyType(), getKeyName(key), key, press);
      }

      return press? sendKeyPress(key): sendKeyRelease(key);
    }

    private boolean sendModifiers (int[] modifiers, boolean press) {
      if (modifiers != null) {
        for (int modifier : modifiers) {
          if (!sendKeyEvent(modifier, press)) return false;
        }
      }

      return true;
    }

    public final boolean sendKeyCombination (int key, int[] modifiers) {
      if (sendModifiers(modifiers, true)) {
        if (sendKeyEvent(key, true)) {
          waitForHoldTime();

          if (sendKeyEvent(key, false)) {
            if (sendModifiers(modifiers, false)) {
              return true;
            }
          }
        }
      }

      return false;
    }

    public final boolean sendKeyCombination (int key) {
      return sendKeyCombination(key, null);
    }
  }

  protected KeyAction (boolean isForDevelopers) {
    super(isForDevelopers);
  }
}
