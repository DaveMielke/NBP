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
  private static final ToggleAction controlModifier = new ToggleAction("control-modifier");

  private static void addSystemKeyChords () {
    final int home = KeyMask.SPACE | KeyMask.DOTS_123456;
    final int back = KeyMask.SPACE | KeyMask.DOTS_12;

    if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      GlobalAction.add(home, AccessibilityService.GLOBAL_ACTION_HOME, "HOME");
      GlobalAction.add(back, AccessibilityService.GLOBAL_ACTION_BACK, "BACK");
    } else if (ApplicationParameters.CHORDS_SEND_SYSTEM_KEYS) {
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

    if (ApplicationParameters.MONITOR_KEYBOARD_DIRECTLY) {
      NodeAction.add(KeyMask.CENTER, AccessibilityNodeInfo.ACTION_CLICK, "click");
    } else {
      KeyCodeAction.add(KeyMask.CENTER, KeyEvent.KEYCODE_DPAD_CENTER);
    }

    KeyCodeAction.add(KeyMask.LEFT, KeyEvent.KEYCODE_DPAD_LEFT);
    KeyCodeAction.add(KeyMask.RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT);
    KeyCodeAction.add(KeyMask.UP, KeyEvent.KEYCODE_DPAD_UP);
    KeyCodeAction.add(KeyMask.DOWN, KeyEvent.KEYCODE_DPAD_DOWN);

    KeyCodeAction.add(KeyMask.DOTS_7, KeyEvent.KEYCODE_DEL);
    KeyCodeAction.add(KeyMask.DOTS_8, KeyEvent.KEYCODE_ENTER);

    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_45), KeyEvent.KEYCODE_TAB);
    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_1456), KeyEvent.KEYCODE_ASSIST);

    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_145), KeyEvent.KEYCODE_FORWARD_DEL);
    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_124), KeyEvent.KEYCODE_SEARCH);
    KeyCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_134), KeyEvent.KEYCODE_MENU);
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1345), AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS, "NOTIFICATIONS", KeyEvent.KEYCODE_NOTIFICATION);
    ScanCodeAction.add((KeyMask.SPACE | KeyMask.DOTS_1478), "POWER", ApplicationUtilities.getGlobalActionTimeout());
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1235), AccessibilityService.GLOBAL_ACTION_RECENTS, "RECENT_APPS");
    ActivityAction.add((KeyMask.SPACE | KeyMask.DOTS_2345), ClockActivity.class);
    Action.add((KeyMask.SPACE | KeyMask.DOTS_1346), controlModifier);

    Action.add((KeyMask.FORWARD), new ForwardAction());
    Action.add((KeyMask.BACKWARD), new BackwardAction());
  }

  private static boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    if (action.performAction()) return true;
    Log.w(LOG_TAG, "action failed: " + action.getName());
    return false;
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
        Action action = Action.getAction(activeKeyMask);
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
    activeKeyMask = 0;
  }

  private Actions () {
  }
}
