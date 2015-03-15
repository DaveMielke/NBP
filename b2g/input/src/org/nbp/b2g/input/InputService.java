package org.nbp.b2g.input;

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

  private final CharacterMap characterMap = new CharacterMap();

  public static InputService getInputService () {
    return inputService;
  }

  @Override
  public void onCreate () {
    super.onCreate();

    Log.d(LOG_TAG, "input service started");
    Actions.add();
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

  protected void logReceivedKeyEvent (int code, boolean press) {
    if (ApplicationParameters.LOG_KEY_EVENTS) {
      StringBuilder sb = new StringBuilder();

      sb.append("key ");
      sb.append((press? "press": "release"));
      sb.append(" received");

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
    logReceivedKeyEvent(code, true);
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
    logReceivedKeyEvent(code, false);
    if (isSystemKey(code)) return false;
    int bit = KeyCode.toKeyMask(code);

    if (bit != 0) {
      if (activeKeyMask > 0) {
        Action action = Action.getAction(activeKeyMask);

        boolean control = Actions.controlModifier.getState();

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

  static {
    System.loadLibrary("InputService");
  }
}
