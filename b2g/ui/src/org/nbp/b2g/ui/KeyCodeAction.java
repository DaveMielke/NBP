package org.nbp.b2g.ui;

import android.util.Log;

import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;

public abstract class KeyCodeAction extends KeyAction {
  private final static String LOG_TAG = KeyCodeAction.class.getName();

  protected final static int NULL_KEY_CODE = KeyEvent.KEYCODE_UNKNOWN;

  protected final static int KEY_CODE_SHIFT = KeyEvent.KEYCODE_SHIFT_LEFT;
  protected final static int KEY_CODE_CONTROL = KeyEvent.KEYCODE_CTRL_LEFT;
  protected final static int KEY_CODE_ALT = KeyEvent.KEYCODE_ALT_LEFT;
  protected final static int KEY_CODE_ALTGR = KeyEvent.KEYCODE_ALT_RIGHT;
  protected final static int KEY_CODE_GUI = KeyEvent.KEYCODE_WINDOW;

  protected int[] getKeyCodeModifiers () {
    return null;
  }

  protected int getKeyCode () {
    return NULL_KEY_CODE;
  }

  @Override
  public boolean performAction () {
    int keyCode = getKeyCode();

    if (keyCode != NULL_KEY_CODE) {
      final InputConnection connection = getInputConnection();

      if (connection != null) {
        KeyCombinationSender keyCombinationSender = new KeyCombinationSender() {
          @Override
          protected boolean sendKeyPress (int key) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, key);
            return connection.sendKeyEvent(event);
          }

          @Override
          protected boolean sendKeyRelease (int key) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_UP, key);
            return connection.sendKeyEvent(event);
          }

          @Override
          protected String getKeyType () {
            return "key code";
          }

          protected String getKeyName (int key) {
            return KeyEvent.keyCodeToString(key);
          }
        };

        if (keyCombinationSender.sendKeyCombination(keyCode, getKeyCodeModifiers())) {
          return true;
        }
      }
    }

    return super.performAction();
  }

  protected KeyCodeAction () {
    super();
  }
}
