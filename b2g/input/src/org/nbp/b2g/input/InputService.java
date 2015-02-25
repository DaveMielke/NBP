package org.nbp.b2g.input;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.os.Bundle;

import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;

public class InputService extends InputMethodService {
  private static final String LOG_TAG = InputService.class.getName();

  private static volatile InputService inputService = null;

  private int pressedKeyMask = 0;
  private int activeKeyMask = 0;

  private Map<Integer, Action> actionMap = new HashMap<Integer, Action>();
  private final CharacterMap characterMap = new CharacterMap();

  public static InputService getInputService () {
    return inputService;
  }

  private void addAction (int keyMask, Action action) {
    actionMap.put(new Integer(keyMask), action);
  }

  private void addKeyAction (int keyMask, int keyCode) {
    addAction(keyMask, new KeyAction(this, keyCode));
  }

  private void addActions () {
    addKeyAction((KeyBit.Dots1), KeyEvent.KEYCODE_DPAD_UP);
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "input service started");
    inputService = this;
    addActions();
  }

  @Override
  public void onDestroy () {
    super.onDestroy();

    Log.d(LOG_TAG, "input service stopped");
    inputService = null;
  }

  @Override
  public void onBindInput () {
    Log.d(LOG_TAG, "input service bound");
  }

  @Override
  public void onUnbindInput () {
    Log.d(LOG_TAG, "input service unbound");
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

  private boolean inputCharacter (char character) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (connection.commitText(Character.toString(character), 1)) {
        return true;
      }
    }

    return false;
  }

  public static void logKeyEvent (int code, boolean press, String description) {
    if (ApplicationParameters.LOG_KEYBOARD_EVENTS) {
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
      case KeyEvent.KEYCODE_DPAD_LEFT:
      case KeyEvent.KEYCODE_DPAD_RIGHT:
      case KeyEvent.KEYCODE_DPAD_UP:
      case KeyEvent.KEYCODE_DPAD_DOWN:
        return true;

      default:
        return false;
    }
  }

  @Override
  public boolean onKeyDown (int code, KeyEvent event) {
    logKeyEventReceived(code, true);
    if (isSystemKey(code)) return false;
    int bit = KeyCode.toKeyBit(code);

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
    int bit = KeyCode.toKeyBit(code);

    if (bit != 0) {
      if (activeKeyMask > 0) {
        Action action = actionMap.get(new Integer(activeKeyMask));

        if (action != null) {
          action.performAction();
        } else if (activeKeyMask <= KeyBit.Space) {
          char character = characterMap.getCharacter(activeKeyMask & ~KeyBit.Space);
          inputCharacter(character);
        }

        activeKeyMask = 0;
      }

      pressedKeyMask &= ~bit;
    }

    return true;
  }
}
