package org.nbp.b2g.input;

import android.util.Log;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.KeyEvent;

public class Actions {
  private static final String LOG_TAG = Actions.class.getName();

  private static int pressedKeyMask = 0;
  private static int activeKeyMask = 0;

  private static final CharacterMap characterMap = new CharacterMap();
  private static final ToggleAction controlModifier = new ToggleAction("CONTROL_MODIFIER");

  private static void addKeyAction (int keyMask, String scanCode, int keyCode) {
    ScanCodeAction.add(keyMask, scanCode);
    KeyCodeAction.add(keyMask, keyCode);
  }

  private static void addSystemKeyChords () {
    final int home = KeyMask.SPACE | KeyMask.DOTS_123456;
    final int back = KeyMask.SPACE | KeyMask.DOTS_12;

    GlobalAction.add((home | KeyMask.SCAN_CODE), AccessibilityService.GLOBAL_ACTION_HOME, "HOME");
    GlobalAction.add((back | KeyMask.SCAN_CODE), AccessibilityService.GLOBAL_ACTION_BACK, "BACK");

    if (ApplicationParameters.CHORDS_SEND_SYSTEM_KEYS) {
      NullAction.add(home);
      NullAction.add(back);
    } else {
      GlobalAction.add(home, AccessibilityService.GLOBAL_ACTION_HOME, "HOME", KeyEvent.KEYCODE_HOME);
      GlobalAction.add(back, AccessibilityService.GLOBAL_ACTION_BACK, "BACK", KeyEvent.KEYCODE_BACK);
    }
  }

  private static void addArrowKeyChords () {
    final int up    = KeyMask.SPACE | KeyMask.DOTS_1;
    final int down  = KeyMask.SPACE | KeyMask.DOTS_4;
    final int left  = KeyMask.SPACE | KeyMask.DOTS_3;
    final int right = KeyMask.SPACE | KeyMask.DOTS_6;

    if (ApplicationParameters.CHORDS_SEND_ARROW_KEYS) {
      NullAction.add(up);
      NullAction.add(down);
      NullAction.add(left);
      NullAction.add(right);
    } else if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      ScanCodeAction.add(up   , "UP");
      ScanCodeAction.add(down , "DOWN");
      ScanCodeAction.add(left , "LEFT");
      ScanCodeAction.add(right, "RIGHT");
    } else {
      KeyCodeAction.add(up   , KeyEvent.KEYCODE_DPAD_UP);
      KeyCodeAction.add(down , KeyEvent.KEYCODE_DPAD_DOWN);
      KeyCodeAction.add(left , KeyEvent.KEYCODE_DPAD_LEFT);
      KeyCodeAction.add(right, KeyEvent.KEYCODE_DPAD_RIGHT);
    }
  }

  public static void add () {
    addSystemKeyChords();
    addArrowKeyChords();

    addKeyAction(KeyMask.VOLUME_DOWN, "VOLUMEDOWN", KeyEvent.KEYCODE_VOLUME_DOWN);
    addKeyAction(KeyMask.VOLUME_UP, "VOLUMEUP", KeyEvent.KEYCODE_VOLUME_UP);

    {
      int keyMask = KeyMask.DPAD_CENTER;

      KeyCodeAction.add(keyMask, KeyEvent.KEYCODE_DPAD_CENTER);
      NodeAction.add((keyMask | KeyMask.SCAN_CODE), AccessibilityNodeInfo.ACTION_CLICK, "CLICK");
    }

    addKeyAction(KeyMask.DPAD_LEFT, "LEFT", KeyEvent.KEYCODE_DPAD_LEFT);
    addKeyAction(KeyMask.DPAD_RIGHT, "RIGHT", KeyEvent.KEYCODE_DPAD_RIGHT);
    addKeyAction(KeyMask.DPAD_UP, "UP", KeyEvent.KEYCODE_DPAD_UP);
    addKeyAction(KeyMask.DPAD_DOWN, "DOWN", KeyEvent.KEYCODE_DPAD_DOWN);

    addKeyAction(KeyMask.DOTS_7, "BACKSPACE", KeyEvent.KEYCODE_DEL);
    addKeyAction(KeyMask.DOTS_8, "ENTER", KeyEvent.KEYCODE_ENTER);

    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_45), "TAB", KeyEvent.KEYCODE_TAB);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_145), "DELETE", KeyEvent.KEYCODE_FORWARD_DEL);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_124), "COMPOSE", KeyEvent.KEYCODE_SEARCH);

    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_1456), KeyEvent.KEYCODE_ASSIST);
    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_134), KeyEvent.KEYCODE_MENU);

    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1345), AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS, "NOTIFICATIONS", KeyEvent.KEYCODE_NOTIFICATION);
    ScanCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_1478), "POWER", ApplicationUtilities.getGlobalActionTimeout());
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1235), AccessibilityService.GLOBAL_ACTION_RECENTS, "RECENT_APPS");
    ActivityAction.add((KeyMask.SPACE | KeyMask.DOTS_2345), ClockActivity.class);
    Action.add((KeyMask.SPACE | KeyMask.DOTS_1346), controlModifier);

    Action.add(KeyMask.FORWARD, new ForwardAction());
    Action.add(KeyMask.BACKWARD, new BackwardAction());
  }

  private static boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    if (action.performAction()) return true;
    Log.w(LOG_TAG, "action failed: " + action.getName());
    return false;
  }

  private static Action getAction (int keyMask) {
    if (KeyboardMonitor.isActive()) {
      Action action = Action.getAction(keyMask | KeyMask.SCAN_CODE);
      if (action != null) return action;
    }

    return Action.getAction(keyMask);
  }

  public static void handleKeyDown (int keyMask) {
    if (keyMask != 0) {
      if ((pressedKeyMask & keyMask) == 0) {
        pressedKeyMask |= keyMask;
        activeKeyMask = pressedKeyMask;
      }
    }
  }

  public static void handleKeyUp (int keyMask) {
    if (keyMask != 0) {
      if (activeKeyMask > 0) {
        Action action = getAction(activeKeyMask);
        boolean control = controlModifier.getState();

        if (action != null) {
          performAction(action);
        } else if (activeKeyMask <= KeyMask.SPACE) {
          char character = characterMap.getCharacter(activeKeyMask & ~KeyMask.SPACE);
          if (control) character &= 0X1F;

          InputService inputService = InputService.getInputService();
          if (inputService != null) {
            inputService.insertCharacter(character);
          }
        }

        activeKeyMask = 0;
      }

      pressedKeyMask &= ~keyMask;
    }
  }

  public static void resetKeys () {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "resetting key state");
    }

    pressedKeyMask = 0;
    activeKeyMask = pressedKeyMask;
  }

  private Actions () {
  }
}
