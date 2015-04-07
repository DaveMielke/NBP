package org.nbp.b2g.ui;

import java.util.SortedSet;
import java.util.TreeSet;

import android.util.Log;

public class KeyEvents {
  private final static String LOG_TAG = KeyEvents.class.getName();

  private static int pressedNavigationKeys = 0;
  private static int activeNavigationKeys = 0;
  private final static SortedSet<Integer> pressedCursorKeys = new TreeSet<Integer>();

  private static boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    try {
      if (action.performAction()) return true;
      Log.w(LOG_TAG, "action failed: " + action.getName());
      ApplicationUtilities.beep();
    } catch (Exception exception) {
      Log.w(LOG_TAG, "action crashed: " + action.getName(), exception);
    }

    return false;
  }

  public static int getNavigationKeys () {
    return activeNavigationKeys;
  }

  private static void handleNavigationKeyPress (int keyMask) {
    if (keyMask != 0) {
      if ((pressedNavigationKeys & keyMask) == 0) {
        pressedNavigationKeys |= keyMask;
        activeNavigationKeys = pressedNavigationKeys;
      }
    }
  }

  private static void handleNavigationKeyRelease (int keyMask) {
    if (keyMask != 0) {
      if (activeNavigationKeys > 0) {
        boolean performed = false;
        Action action = Context.getCurrentContext().getKeyBindings().getAction(activeNavigationKeys);

        if (action != null) {
          if (performAction(action)) {
            performed = true;
          }
        }

        if (!performed) ApplicationUtilities.beep();
        activeNavigationKeys = 0;
      }

      pressedNavigationKeys &= ~keyMask;
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
