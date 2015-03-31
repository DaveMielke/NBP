package org.nbp.b2g.ui;

import java.util.SortedSet;
import java.util.TreeSet;

import android.util.Log;

public class KeyEvents {
  private final static String LOG_TAG = KeyEvents.class.getName();

  private static int pressedNavigationKeys = 0;
  private static int activeNavigationKeys = 0;
  private final static SortedSet<Integer> pressedRoutingKeys = new TreeSet<Integer>();

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
        Action action = KeyBindings.getAction(activeNavigationKeys);

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

  public static int[] getRoutingKeys () {
    int[] keyNumbers = new int[pressedRoutingKeys.size()];
    int index = 0;

    for (int keyNumber : pressedRoutingKeys) {
      keyNumbers[index++] = keyNumber;
    }

    return keyNumbers;
  }

  private static void handleRoutingKeyPress (int keyNumber) {
    pressedRoutingKeys.add(keyNumber);
  }

  private static void handleRoutingKeyRelease (int keyNumber) {
    pressedRoutingKeys.remove(keyNumber);
  }

  public static void handleRoutingKeyEvent (int keyNumber, boolean press) {
    if (press) {
      handleRoutingKeyPress(keyNumber);
    } else {
      handleRoutingKeyRelease(keyNumber);
    }
  }

  public static void resetKeys () {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "resetting key state");
    }

    pressedNavigationKeys = 0;
    activeNavigationKeys = pressedNavigationKeys;

    pressedRoutingKeys.clear();
  }

  private KeyEvents () {
  }
}
