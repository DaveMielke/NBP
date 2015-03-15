package org.nbp.b2g.input;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class Actions {
  public static final ToggleAction controlModifier = new ToggleAction("control-modifier");

  private static void addSystemKeyChords () {
    final int home = KeyMask.SPACE | KeyMask.DOTS_123456;
    final int back = KeyMask.SPACE | KeyMask.DOTS_12;

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
    } else {
      KeyAction.add(up   , KeyEvent.KEYCODE_DPAD_UP);
      KeyAction.add(down , KeyEvent.KEYCODE_DPAD_DOWN);
      KeyAction.add(left , KeyEvent.KEYCODE_DPAD_LEFT);
      KeyAction.add(right, KeyEvent.KEYCODE_DPAD_RIGHT);
    }
  }

  public static void add () {
    addSystemKeyChords();
    addArrowKeyChords();

    KeyAction.add(KeyMask.CENTER, KeyEvent.KEYCODE_DPAD_CENTER);
    KeyAction.add(KeyMask.LEFT, KeyEvent.KEYCODE_DPAD_LEFT);
    KeyAction.add(KeyMask.RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT);
    KeyAction.add(KeyMask.UP, KeyEvent.KEYCODE_DPAD_UP);
    KeyAction.add(KeyMask.DOWN, KeyEvent.KEYCODE_DPAD_DOWN);

    KeyAction.add(KeyMask.DOTS_7, KeyEvent.KEYCODE_DEL);
    KeyAction.add(KeyMask.DOTS_8, KeyEvent.KEYCODE_ENTER);

    KeyAction.add((KeyMask.SPACE | KeyMask.DOTS_45), KeyEvent.KEYCODE_TAB);
    KeyAction.add((KeyMask.SPACE | KeyMask.DOTS_1456), KeyEvent.KEYCODE_ASSIST);

    KeyAction.add((KeyMask.SPACE | KeyMask.DOTS_145), KeyEvent.KEYCODE_FORWARD_DEL);
    KeyAction.add((KeyMask.SPACE | KeyMask.DOTS_124), KeyEvent.KEYCODE_SEARCH);
    KeyAction.add((KeyMask.SPACE | KeyMask.DOTS_134), KeyEvent.KEYCODE_MENU);
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1345), AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS, "NOTIFICATIONS", KeyEvent.KEYCODE_NOTIFICATION);
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1478), new PowerAction());
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1235), AccessibilityService.GLOBAL_ACTION_RECENTS, "RECENT_APPS");
    ActivityAction.add((KeyMask.SPACE | KeyMask.DOTS_2345), ClockActivity.class);
    Action.add((KeyMask.SPACE | KeyMask.DOTS_1346), controlModifier);

    Action.add((KeyMask.FORWARD), new ForwardAction());
    Action.add((KeyMask.BACKWARD), new BackwardAction());
  }

  private Actions () {
  }
}
