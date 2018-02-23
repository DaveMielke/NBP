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

import java.util.concurrent.Callable;

import org.nbp.common.Timeout;
import org.nbp.common.Tones;

import android.util.Log;
import android.os.SystemClock;
import android.os.PowerManager;

public abstract class KeyEvents {
  private final static String LOG_TAG = KeyEvents.class.getName();

  private KeyEvents () {
  }

  private static long navigationKeyReleaseTime;
  private static int activeNavigationKeys;
  private static int pressedNavigationKeys;
  private final static SortedSet<Integer> pressedCursorKeys = new TreeSet<Integer>();

  private final static int oneHandCompletionKey = KeyMask.SPACE;
  private static boolean oneHandNavigationKeyPressed;
  private static long oneHandSpaceTimeout;

  private final static int oneHandImmediateKeys = KeyMask.CURSOR
                                                | KeyMask.GROUP_PAN
                                                | KeyMask.GROUP_DPAD
                                                | KeyMask.GROUP_VOLUME
                                                ;

  public static boolean performAction (final Action action) {
    if (action.editsInput()) {
      if (!ApplicationSettings.EDITING_ENABLED) {
        ApplicationUtilities.message(R.string.error_editing_off);
        return false;
      }
    }

    if (ApplicationSettings.LOG_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    Boolean result = Crash.runComponent(
      "action", action.getName(),
      new Callable<Boolean>() {
        @Override
        public Boolean call () {
          if (action.performAction()) {
            Integer confirmation = action.getConfirmation();
            if (confirmation != null) ApplicationUtilities.message(confirmation);
            return true;
          }

          Log.w(LOG_TAG, "action failed: " + action.getName());
          return false;
        }
      }
    );

    if (result == null) return false;
    return result;
  }

  public static boolean performAction (Action action, int... cursorKeys) {
    for (int key : cursorKeys) pressedCursorKeys.add(key);
    boolean result = performAction(action);
    for (int key : cursorKeys) pressedCursorKeys.remove(key);
    return result;
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
    Endpoint endpoint = Endpoints.getCurrentEndpoint();
    KeyBindings keyBindings = endpoint.getKeyBindings();
    Action action = null;

    if (isLongPress && ApplicationSettings.LONG_PRESS) {
      action = keyBindings.getAction(keys | KeyMask.LONG_PRESS);
    }

    if (action == null) {
      action = keyBindings.getAction(keys);
    }

    if (action == null) {
      if (endpoint instanceof InputEndpoint) {
        if (keyBindings.isRootKeyBindings()) {
          if (KeyMask.isDots(keys)) {
            action = keyBindings.getAction(TypeCharacter.class);
          }
        }
      }
    }

    if (action != null) {
      if (ApplicationSettings.REVERSE_PANNING) {
        if (((keys & KeyMask.FORWARD) != 0) != ((keys & KeyMask.BACKWARD) != 0)) {
          Action reverse = keyBindings.getAction(reversePanningActionMap.get((Class<? extends Action>)action.getClass()));
          if (reverse != null) action = reverse;
        }
      }
    }

    return action;
  }

  private static boolean performAction (boolean isLongPress) {
    int keys = activeNavigationKeys;
    if (keys == 0) return true;
    boolean wasModifier = false;

    try {
      Action action = getAction(keys, isLongPress);
      boolean performed = false;

      if (action != null) {
        if (action instanceof ModifierAction) wasModifier = true;
        if (!action.isHidden()) Devices.braille.get().dismiss();
        if (performAction(action)) performed = true;
      }

      if (!performed) Tones.beep();
      return performed;
    } finally {
      activeNavigationKeys = 0;
      if (!wasModifier) ModifierAction.cancelModifiers();
    }
  }

  private final static Object AWAKEN_LOCK = new Object();
  private static PowerManager.WakeLock keyEventWakeLock = null;

  private static void awakenSystem () {
    synchronized (AWAKEN_LOCK) {
      if (keyEventWakeLock == null) {
        keyEventWakeLock = ApplicationContext.newWakeLock(
          PowerManager.ACQUIRE_CAUSES_WAKEUP |
          PowerManager.ON_AFTER_RELEASE |
          PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
          "key-event"
        );
      }

      keyEventWakeLock.acquire();
      keyEventWakeLock.release();
    }
  }

  private static void onKeyPress () {
    awakenSystem();
  }

  private static Timeout longPressTimeout = new Timeout(ApplicationParameters.LONG_PRESS_TIME, "long-press-timeout") {
    @Override
    public void run () {
      performAction(true);
    }
  };

  public final static void resetKeys () {
    synchronized (longPressTimeout) {
      if (ApplicationSettings.LOG_KEYBOARD) {
        Log.d(LOG_TAG, "resetting key state");
      }

      navigationKeyReleaseTime = 0;
      activeNavigationKeys = 0;
      pressedNavigationKeys = 0;
      pressedCursorKeys.clear();

      oneHandNavigationKeyPressed = false;
      oneHandSpaceTimeout = 0;
    }
  }

  static {
    resetKeys();
  }

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

  private static int handleEndpointNavigationKeyEvent (int keyMask, boolean press) {
    int alreadyPressedKeys = keyMask & pressedNavigationKeys;

    return Endpoints.getCurrentEndpoint().handleNavigationKeyEvent((keyMask & ~alreadyPressedKeys), press)
         | alreadyPressedKeys;
  }

  private static void handleNavigationKeyPress (int keyMask) {
    onKeyPress();
    keyMask = handleEndpointNavigationKeyEvent(keyMask, true);

    if (keyMask != 0) {
      synchronized (longPressTimeout) {
        boolean isFirstPress = pressedNavigationKeys == 0;

        pressedNavigationKeys |= keyMask;
        logNavigationKeysChange(keyMask, "press");

        if (ApplicationSettings.ONE_HAND) {
          if (isFirstPress) {
            if ((navigationKeyReleaseTime + ApplicationSettings.PRESSED_TIMEOUT) < SystemClock.elapsedRealtime()) {
              activeNavigationKeys = 0;
            }
          }

          activeNavigationKeys |= keyMask & ~oneHandCompletionKey;
          oneHandNavigationKeyPressed = true;
        } else {
          activeNavigationKeys = pressedNavigationKeys;
          longPressTimeout.start();
        }
      }
    }
  }

  private static void handleNavigationKeyRelease (int keyMask) {
    keyMask = handleEndpointNavigationKeyEvent(keyMask, false);

    if (keyMask != 0) {
      synchronized (longPressTimeout) {
        long now = SystemClock.elapsedRealtime();
        navigationKeyReleaseTime = now;

        boolean isComplete = !ApplicationSettings.ONE_HAND
                           || ((activeNavigationKeys & oneHandImmediateKeys) != 0)
                           ;

        if (oneHandNavigationKeyPressed) {
          // first key release of a fully pressed combination
          oneHandNavigationKeyPressed = false;

          // capture and reset the space timeout so that only one space can be quick
          long spaceTimeout = oneHandSpaceTimeout;
          oneHandSpaceTimeout = 0;

          if ((pressedNavigationKeys & oneHandCompletionKey) != 0) {
            // the combination included Space

            if ((pressedNavigationKeys & ~oneHandCompletionKey) != 0) {
              // the combination also included at least one other key
              activeNavigationKeys |= oneHandCompletionKey;
              isComplete = true;
            } else if (activeNavigationKeys != 0) {
              // just Space was rpessed so complete the pending combination
              isComplete = true;

              if (activeNavigationKeys != oneHandCompletionKey) {
                // start the quick space timeout except after Space itself
                oneHandSpaceTimeout = now + ApplicationSettings.SPACE_TIMEOUT;
              }
            } else {
              // it's an initial Space so start a new combination with it
              activeNavigationKeys = oneHandCompletionKey;
              if (SystemClock.elapsedRealtime() < spaceTimeout) isComplete = true;
            }
          }
        }

        longPressTimeout.cancel();
        pressedNavigationKeys &= ~keyMask;
        logNavigationKeysChange(keyMask, "release");
        if (isComplete) performAction(false);
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

  public static void handleNavigationKey (int keyMask) {
    handleNavigationKeyPress(keyMask);
    handleNavigationKeyRelease(keyMask);
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

  private static boolean handleEndpointCursorKeyEvent (int keyNumber, boolean press) {
    return Endpoints.getCurrentEndpoint().handleCursorKeyEvent(keyNumber, press);
  }

  private static void handleCursorKeyPress (int keyNumber) {
    onKeyPress();
    if (handleEndpointCursorKeyEvent(keyNumber, true)) return;

    synchronized (longPressTimeout) {
      pressedCursorKeys.add(keyNumber);
      logCursorKeyAction(keyNumber, "press");

      if (pressedCursorKeys.size() == 1) {
        handleNavigationKey(KeyMask.CURSOR);
      }
    }
  }

  private static void handleCursorKeyRelease (int keyNumber) {
    if (handleEndpointCursorKeyEvent(keyNumber, false)) return;

    synchronized (longPressTimeout) {
      pressedCursorKeys.remove(keyNumber);
      logCursorKeyAction(keyNumber, "release");
    }
  }

  public static void handleCursorKeyEvent (int keyNumber, boolean press) {
    if (press) {
      handleCursorKeyPress(keyNumber);
    } else {
      handleCursorKeyRelease(keyNumber);
    }
  }
}
