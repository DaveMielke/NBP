package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

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

  private Map<Integer, Action> actionMap = new HashMap<Integer, Action>();
  private final CharacterMap characterMap = new CharacterMap();
  private boolean controlModifier = false;

  public static InputService getInputService () {
    return inputService;
  }

  private void addAction (int keyMask, Action action) {
    actionMap.put(new Integer(keyMask), action);
  }

  private void addNullAction (int keyMask) {
    addAction(keyMask, new Action());
  }

  private void addActivityAction (int keyMask, Class activityClass) {
    addAction(keyMask, new ActivityAction(activityClass));
  }

  private void addKeyAction (int keyMask, int keyCode, int globalAction) {
    addAction(keyMask, new KeyAction(keyCode, globalAction));
  }

  private void addKeyAction (int keyMask, int keyCode) {
    addAction(keyMask, new KeyAction(keyCode));
  }

  private void addActions () {
    if (ApplicationParameters.CHORDS_SEND_SYSTEM_KEYS) {
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_123456));
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_12));
    } else {
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_123456), KeyEvent.KEYCODE_HOME);
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_12), KeyEvent.KEYCODE_BACK, AccessibilityService.GLOBAL_ACTION_BACK);
    }

    addKeyAction(KeyMask.CENTER, KeyEvent.KEYCODE_DPAD_CENTER);
    addKeyAction(KeyMask.LEFT, KeyEvent.KEYCODE_DPAD_LEFT);
    addKeyAction(KeyMask.RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT);
    addKeyAction(KeyMask.UP, KeyEvent.KEYCODE_DPAD_UP);
    addKeyAction(KeyMask.DOWN, KeyEvent.KEYCODE_DPAD_DOWN);

    if (ApplicationParameters.CHORDS_SEND_ARROW_KEYS) {
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_1));
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_4));
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_3));
      addNullAction((KeyMask.SPACE | KeyMask.DOTS_6));
    } else {
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_1), KeyEvent.KEYCODE_DPAD_UP);
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_4), KeyEvent.KEYCODE_DPAD_DOWN);
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_3), KeyEvent.KEYCODE_DPAD_LEFT);
      addKeyAction((KeyMask.SPACE | KeyMask.DOTS_6), KeyEvent.KEYCODE_DPAD_RIGHT);
    }

    addKeyAction(KeyMask.DOTS_7, KeyEvent.KEYCODE_DEL);
    addKeyAction(KeyMask.DOTS_8, KeyEvent.KEYCODE_ENTER);

    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_45), KeyEvent.KEYCODE_TAB);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_1456), KeyEvent.KEYCODE_ASSIST);

    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_145), KeyEvent.KEYCODE_DEL);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_124), KeyEvent.KEYCODE_SEARCH);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_134), KeyEvent.KEYCODE_MENU);
    addKeyAction((KeyMask.SPACE | KeyMask.DOTS_1345), KeyEvent.KEYCODE_NOTIFICATION, AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
    addActivityAction((KeyMask.SPACE | KeyMask.DOTS_2345), ClockActivity.class);

    addAction((KeyMask.SPACE | KeyMask.DOTS_1346), new Action() {
      @Override
      public final boolean performAction () {
        controlModifier = !controlModifier;
        return true;
      }
    });
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
      Log.d(LOG_TAG, "inserting character: " + character);
    }

    if (connection != null) {
      if (connection.commitText(Character.toString(character), 1)) {
        return true;
      }
    }

    Log.w(LOG_TAG, "character insertion failed: " + character);
    return false;
  }

  private boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getActionName());
    }

    if (action.performAction()) return true;
    Log.w(LOG_TAG, "action failed: " + action.getActionName());
    return false;
  }

  public static void logKeyEvent (int code, boolean press, String description) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key ");
      sb.append((press? "press": "release"));
      sb.append(' ');
      sb.append(description);

      sb.append(": ");
      sb.append(code);
      sb.append(" (");
      sb.append(KeyEvent.keyCodeToString(code));
      sb.append(")");

      Log.d(LOG_TAG, sb.toString());
    }
  }

  public static void logKeyEventReceived (int code, boolean press) {
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
        Action action = actionMap.get(new Integer(activeKeyMask));

        boolean control = controlModifier;
        controlModifier = false;

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
