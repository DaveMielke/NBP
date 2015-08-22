package org.nbp.b2g.ui;

import org.nbp.b2g.ui.actions.TypeCharacter;
import org.nbp.b2g.ui.actions.PanLeft;
import org.nbp.b2g.ui.actions.PanRight;
import org.nbp.b2g.ui.host.actions.MoveBackward;
import org.nbp.b2g.ui.host.actions.MoveForward;

import java.util.SortedSet;
import java.util.TreeSet;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public abstract class KeyEvents {
  private final static String LOG_TAG = KeyEvents.class.getName();

  private static int pressedNavigationKeys = 0;
  private static int activeNavigationKeys = 0;
  private final static SortedSet<Integer> pressedCursorKeys = new TreeSet<Integer>();

  private static boolean performAction (Action action) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    try {
      if (action.performAction()) {
        Integer confirmation = action.getConfirmation();
        if (confirmation != null) ApplicationUtilities.message(confirmation);
        return true;
      }

      Log.w(LOG_TAG, "action failed: " + action.getName());
    } catch (Exception exception) {
      Crash.handleCrash(exception, "action", action.getName());
    }

    return false;
  }

  private static Action getAction (Class<? extends Action> type, Endpoint endpoint) {
    if (type == null) return null;
    return endpoint.getKeyBindings().getAction(type);
  }

  public static boolean performAction (Class<? extends Action> type, Endpoint endpoint) {
    Action action = getAction(type, endpoint);
    if (action == null) return false;
    return performAction(action);
  }

  private final static Map<Class<? extends Action>, Class<? extends Action>> reversePanningActionMap =
    new HashMap<Class<? extends Action>, Class<? extends Action>>();

  static {
    reversePanningActionMap.put(PanLeft.class, PanRight.class);
    reversePanningActionMap.put(PanRight.class, PanLeft.class);

    reversePanningActionMap.put(MoveBackward.class, MoveForward.class);
    reversePanningActionMap.put(MoveForward.class, MoveBackward.class);
  }

  private static Action getAction (int keys, boolean isLongPress) {
    KeyBindings keyBindings = Endpoints.getCurrentEndpoint().getKeyBindings();
    Action action = null;

    if (isLongPress && ApplicationSettings.LONG_PRESS) {
      action = keyBindings.getAction(keys | KeyMask.LONG_PRESS);
    }

    if (action == null) {
      action = keyBindings.getAction(keys);
    }

    if (action == null) {
      if (keyBindings.isRootKeyBindings()) {
        if (KeyMask.isDots(keys)) {
          action = keyBindings.getAction(TypeCharacter.class);
        }
      }
    }

    if (action != null) {
      if (ApplicationSettings.REVERSE_PANNING) {
        if (((keys & KeyMask.FORWARD) != 0) != ((keys & KeyMask.BACKWARD) != 0)) {
          Action reverse = keyBindings.getAction(reversePanningActionMap.get((Class <? extends Action>)action.getClass()));
          if (reverse != null) action = reverse;
        }
      }
    }

    return action;
  }

  private static boolean performAction (boolean isLongPress) {
    boolean wasModifier = false;

    int keys = activeNavigationKeys;
    if (keys == 0) return true;

    try {
      Action action = getAction(keys, isLongPress);
      boolean performed = false;

      if (action != null) {
        if (action instanceof ModifierAction) wasModifier = true;
        Devices.braille.get().dismiss();

        if (performAction(action)) {
          performed = true;
        }
      }

      if (!performed) Devices.tone.get().beep();
      return performed;
    } finally {
      activeNavigationKeys = 0;
      if (!wasModifier) ModifierAction.cancelModifiers();
    }
  }

  private static Timeout longPressTimeout = new Timeout(ApplicationParameters.LONG_PRESS_TIME, "long-key-press-timeout") {
    @Override
    public void run () {
      performAction(true);
    }
  };

  public static int getNavigationKeys () {
    return activeNavigationKeys;
  }

  private static void logNavigationKeysChange (int keyMask, String action) {
    if (ApplicationSettings.LOG_KEYBOARD) {
      StringBuilder sb = new StringBuilder();

      sb.append("navigation key ");
      sb.append(action);

      int[] masks = new int[] {keyMask, pressedNavigationKeys};
      String delimiter = ": ";

      for (int mask : masks) {
        sb.append(delimiter);
        delimiter = " -> ";

        sb.append(String.format("0X%02X", mask));
        sb.append(" (");
        sb.append(KeyMask.toString(mask));
        sb.append(')');
      }

      Log.d(LOG_TAG, sb.toString());
    }
  }

  private static void handleNavigationKeyPress (int keyMask) {
    if (keyMask != 0) {
      synchronized (longPressTimeout) {
        pressedNavigationKeys |= keyMask;
        logNavigationKeysChange(keyMask, "press");

        if (!ApplicationSettings.ONE_HAND) {
          activeNavigationKeys = pressedNavigationKeys;
          longPressTimeout.start();
        } else if ((keyMask == KeyMask.SPACE) && (activeNavigationKeys != 0)) {
          performAction(false);
          pressedCursorKeys.clear();
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
        logNavigationKeysChange(keyMask, "release");

        if (!ApplicationSettings.ONE_HAND) {
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

  private static void logCursorKeyAction (int keyNumber, String action) {
    if (ApplicationSettings.LOG_KEYBOARD) {
      StringBuilder sb = new StringBuilder();

      sb.append("cursor key ");
      sb.append(action);

      sb.append(": ");
      sb.append(keyNumber);

      sb.append(" (");
      String delimiter = "";
      for (Integer key : pressedCursorKeys) {
        sb.append(delimiter);
        delimiter = ", ";
        sb.append(key);
      }
      sb.append(')');

      Log.d(LOG_TAG, sb.toString());
    }
  }

  private static void handleCursorKeyPress (int keyNumber) {
    if (ApplicationSettings.ONE_HAND) {
      pressedCursorKeys.clear();
    }

    pressedCursorKeys.add(keyNumber);
    logCursorKeyAction(keyNumber, "press");

    if (pressedCursorKeys.size() == 1) {
      handleNavigationKeyPress(KeyMask.CURSOR);
      handleNavigationKeyRelease(KeyMask.CURSOR);
    }
  }

  private static void handleCursorKeyRelease (int keyNumber) {
    if (!ApplicationSettings.ONE_HAND) {
      pressedCursorKeys.remove(keyNumber);
    }

    logCursorKeyAction(keyNumber, "release");
  }

  public static void handleCursorKeyEvent (int keyNumber, boolean press) {
    if (press) {
      handleCursorKeyPress(keyNumber);
    } else {
      handleCursorKeyRelease(keyNumber);
    }
  }

  public static void resetKeys () {
    if (ApplicationSettings.LOG_KEYBOARD) {
      Log.d(LOG_TAG, "resetting key state");
    }

    pressedNavigationKeys = 0;
    activeNavigationKeys = pressedNavigationKeys;

    pressedCursorKeys.clear();
  }

  private KeyEvents () {
  }
}
