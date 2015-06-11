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

  protected abstract class KeyCombinationInjector {
    protected abstract boolean injectKeyPress (int key);
    protected abstract boolean injectKeyRelease (int key);

    private boolean injectKeyEvent (int key, boolean press) {
      return press? injectKeyPress(key): injectKeyRelease(key);
    }

    private boolean injectModifiers (int[] modifiers, boolean press) {
      if (modifiers != null) {
        for (int modifier : modifiers) {
          if (!injectKeyEvent(modifier, press)) return false;
        }
      }

      return true;
    }

    public final boolean injectKeyCombination (int key, int[] modifiers) {
      if (injectModifiers(modifiers, true)) {
        if (injectKeyEvent(key, true)) {
          waitForHoldTime();

          if (injectKeyEvent(key, false)) {
            if (injectModifiers(modifiers, false)) {
              return true;
            }
          }
        }
      }

      return false;
    }

    public final boolean injectKeyCombination (int key) {
      return injectKeyCombination(key, null);
    }
  }

  protected KeyAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
