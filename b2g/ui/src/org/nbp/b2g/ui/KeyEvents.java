package org.nbp.b2g.ui;

import java.util.SortedSet;
import java.util.TreeSet;

import android.util.Log;

public class KeyEvents {
  private final static String LOG_TAG = KeyEvents.class.getName();

  private static int pressedNavigationKeys = 0;
  private static int activeNavigationKeys = 0;
  private final static SortedSet<Integer> pressedCursorKeys = new TreeSet<Integer>();

  private static boolean performAction (Action action, boolean isLongPress) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    try {
      if (action.performAction(isLongPress)) return true;
      Log.w(LOG_TAG, "action failed: " + action.getName());
      ApplicationUtilities.beep();
    } catch (Exception exception) {
      Log.w(LOG_TAG, "action crashed: " + action.getName(), exception);
    }

    return false;
  }

  private static void performAction (boolean isLongPress) {
    if (activeNavigationKeys != 0) {
      boolean performed = false;
      KeyBindings keyBindings = Endpoints.getCurrentEndpoint().getKeyBindings();
      Action action = null;

      if (isLongPress) {
        action = keyBindings.getAction(activeNavigationKeys | KeyMask.LONG_PRESS);
      }

      if (action == null) {
        action = keyBindings.getAction(activeNavigationKeys);
      }

      if (action != null) {
        if (performAction(action, isLongPress)) {
          performed = true;
        }
      }

      if (!performed) ApplicationUtilities.beep();
      activeNavigationKeys = 0;
    }
  }

  private static Timeout longPressTimeout = new Timeout(ApplicationParameters.LONG_PRESS_TIME) {
    @Override
    public void run () {
      performAction(true);
    }
  };

  public static int getNavigationKeys () {
    return activeNavigationKeys;
  }

  private static void handleNavigationKeyPress (int keyMask) {
    if (keyMask != 0) {
      synchronized (longPressTimeout) {
        pressedNavigationKeys |= keyMask;

        if (!ApplicationParameters.ONE_HAND_MODE) {
          activeNavigationKeys = pressedNavigationKeys;
          longPressTimeout.start();
        } else if ((keyMask == KeyMask.SPACE) && (activeNavigationKeys != 0)) {
          performAction(false);
        } else {
          activeNavigationKeys |= keyMask;
        }
      }
    }
  }

  private static void handleNavigationKeyRelease (int keyMask) {
    if (keyMask != 0) {
      synchronized (longPressTimeout) {
        longPressTimeout.cancel();
        pressedNavigationKeys &= ~keyMask;

        if (!ApplicationParameters.ONE_HAND_MODE) {
          performAction(false);
        }
      }
    }
  }

  public static void handleNavigationKeyEvent (int keyMask, boolean press) {
    if (press) {
      handleNavigationKeyPress(keyMask);
    } else {
      handleNavigationKeyRelease(keyMask);
    }
  }

  public static int[] getCursorKeys () {
    int[] keyNumbers = new int[pressedCursorKeys.size()];
    int index = 0;

    for (int keyNumber : pressedCursorKeys) {
      keyNumbers[index++] = keyNumber;
    }

    return keyNumbers;
  }

  private static void handleCursorKeyPress (int keyNumber) {
    pressedCursorKeys.add(keyNumber);

    if (pressedCursorKeys.size() == 1) {
      handleNavigationKeyPress(KeyMask.CURSOR);
      handleNavigationKeyRelease(KeyMask.CURSOR);
    }
  }

  private static void handleCursorKeyRelease (int keyNumber) {
    pressedCursorKeys.remove(keyNumber);
  }

  public static void handleCursorKeyEvent (int keyNumber, boolean press) {
    if (press) {
      handleCursorKeyPress(keyNumber);
    } else {
      handleCursorKeyRelease(keyNumber);
    }
  }

  public static void resetKeys () {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "resetting key state");
    }

    pressedNavigationKeys = 0;
    activeNavigationKeys = pressedNavigationKeys;

    pressedCursorKeys.clear();
  }

  private KeyEvents () {
  }
}
