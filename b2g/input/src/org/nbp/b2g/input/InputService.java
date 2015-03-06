package org.nbp.b2g.input;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;

import android.content.Intent;
import android.accessibilityservice.AccessibilityService;

public class InputService extends InputMethodService {
  private static final String LOG_TAG = InputService.class.getName();

  private static volatile InputService inputService = null;

  private int pressedKeyMask = 0;
  private int activeKeyMask = 0;

  private final CharacterMap characterMap = new CharacterMap();
  private final ToggleAction controlModifier = new ToggleAction("control-modifier");

  public static InputService getInputService () {
    return inputService;
  }

  private void addSystemKeyChords () {
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

  private void addArrowKeyChords () {
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

  private void addActions () {
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
    GlobalAction.add((KeyMask.SPACE | KeyMask.DOTS_1235), AccessibilityService.GLOBAL_ACTION_RECENTS, "RECENT_APPS");
    ActivityAction.add((KeyMask.SPACE | KeyMask.DOTS_2345), ClockActivity.class);
    Action.add((KeyMask.SPACE | KeyMask.DOTS_1346), controlModifier);
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "input service started");
    addActions();
  }

  @Override
  public void onDestroy () {
    super.onDestroy();

    Log.d(LOG_TAG, "input service stopped");
  }

  @Override
  public void onBindInput () {
    Log.d(LOG_TAG, "input service bound");
    inputService = this;
  }

  @Override
  public void onUnbindInput () {
    Log.d(LOG_TAG, "input service unbound");
    inputService = null;

    if (ApplicationParameters.LOG_KEY_EVENTS) {
      Log.d(LOG_TAG, "resetting key state");
    }

    pressedKeyMask = 0;
    activeKeyMask = 0;
  }

  @Override
  public void onStartInput (EditorInfo info, boolean restarting) {
    Log.d(LOG_TAG, "input service " + (restarting? "reconnected": "connected"));
  }

  @Override
  public void onFinishInput () {
    Log.d(LOG_TAG, "input service disconnected");
  }

  private InputConnection getInputConnection () {
    InputConnection connection = getCurrentInputConnection();

    if (connection != null) {
      return connection;
    } else {
      Log.w(LOG_TAG, "input service not connected");
    }

    return null;
  }

  private boolean insertCharacter (char character) {
    InputConnection connection = getInputConnection();

    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, String.format("inserting character: 0X%02X", (int)character));
    }

    if (connection != null) {
      if (connection.commitText(Character.toString(character), 1)) {
        return true;
      }
    }

    Log.w(LOG_TAG, String.format("character insertion failed: 0X%02X", (int)character));
    return false;
  }

  private boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    if (action.performAction()) return true;
    Log.w(LOG_TAG, "action failed: " + action.getName());
    return false;
  }

  protected void logKeyEvent (int code, boolean press, String action) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key ");
      sb.append((press? "press": "release"));
      sb.append(' ');
      sb.append(action);

      sb.append(": ");
      sb.append(code);
      sb.append(" (");
      sb.append(KeyEvent.keyCodeToString(code));
      sb.append(")");

      sb.append(String.format(": Pkm:0X%04X", pressedKeyMask));
      sb.append(String.format(": Akm:0X%04X", activeKeyMask));

      Log.d(LOG_TAG, sb.toString());
    }
  }

  protected void logKeyEventReceived (int code, boolean press) {
    logKeyEvent(code, press, "received");
  }

  public static boolean isSystemKey (int code) {
    switch (code) {
      case KeyEvent.KEYCODE_HOME:
      case KeyEvent.KEYCODE_BACK:
        return true;

      default:
        return false;
    }
  }

  @Override
  public boolean onKeyDown (int code, KeyEvent event) {
    logKeyEventReceived(code, true);
    if (isSystemKey(code)) return false;
    int bit = KeyCode.toKeyMask(code);

    if (bit != 0) {
      if ((pressedKeyMask & bit) == 0) {
        pressedKeyMask |= bit;
        activeKeyMask = pressedKeyMask;
      }
    }

    return true;
  }

  @Override
  public boolean onKeyUp (int code, KeyEvent event) {
    logKeyEventReceived(code, false);
    if (isSystemKey(code)) return false;
    int bit = KeyCode.toKeyMask(code);

    if (bit != 0) {
      if (activeKeyMask > 0) {
        Action action = Action.getAction(activeKeyMask);

        boolean control = controlModifier.getState();

        if (action != null) {
          performAction(action);
        } else if (activeKeyMask <= KeyMask.SPACE) {
          char character = characterMap.getCharacter(activeKeyMask & ~KeyMask.SPACE);
          if (control) character &= 0X1F;
          insertCharacter(character);
        }

        activeKeyMask = 0;
      }

      pressedKeyMask &= ~bit;
    }

    return true;
  }
}
