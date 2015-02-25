package org.nbp.b2g.input;

import java.util.Arrays;
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

  private final static char[] characterMap = new char[0X100];
  private Map<Integer, Action> actionMap = new HashMap<Integer, Action>();

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
          inputCharacter(characterMap[activeKeyMask & ~KeyBit.Space]);
        }

        activeKeyMask = 0;
      }

      pressedKeyMask &= ~bit;
    }

    return true;
  }

  private static void fillCharacterMap () {
    Arrays.fill(characterMap, '?');
    characterMap[KeyBit.DotsNone] = ' ';

    characterMap[KeyBit.Dots1] = 'a';
    characterMap[KeyBit.Dots12] = 'b';
    characterMap[KeyBit.Dots14] = 'c';
    characterMap[KeyBit.Dots145] = 'd';
    characterMap[KeyBit.Dots15] = 'e';
    characterMap[KeyBit.Dots124] = 'f';
    characterMap[KeyBit.Dots1245] = 'g';
    characterMap[KeyBit.Dots125] = 'h';
    characterMap[KeyBit.Dots24] = 'i';
    characterMap[KeyBit.Dots245] = 'j';

    characterMap[KeyBit.Dots13] = 'k';
    characterMap[KeyBit.Dots123] = 'l';
    characterMap[KeyBit.Dots134] = 'm';
    characterMap[KeyBit.Dots1345] = 'n';
    characterMap[KeyBit.Dots135] = 'o';
    characterMap[KeyBit.Dots1234] = 'p';
    characterMap[KeyBit.Dots12345] = 'q';
    characterMap[KeyBit.Dots1235] = 'r';
    characterMap[KeyBit.Dots234] = 's';
    characterMap[KeyBit.Dots2345] = 't';

    characterMap[KeyBit.Dots136] = 'u';
    characterMap[KeyBit.Dots1236] = 'v';
    characterMap[KeyBit.Dots1346] = 'x';
    characterMap[KeyBit.Dots13456] = 'y';
    characterMap[KeyBit.Dots1356] = 'z';
    characterMap[KeyBit.Dots12346] = '&';
    characterMap[KeyBit.Dots123456] = '=';
    characterMap[KeyBit.Dots12356] = '(';
    characterMap[KeyBit.Dots2346] = '!';
    characterMap[KeyBit.Dots23456] = ')';

    characterMap[KeyBit.Dots16] = '*';
    characterMap[KeyBit.Dots126] = '<';
    characterMap[KeyBit.Dots146] = '%';
    characterMap[KeyBit.Dots1456] = '?';
    characterMap[KeyBit.Dots156] = ':';
    characterMap[KeyBit.Dots1246] = '$';
    characterMap[KeyBit.Dots12456] = '}';
    characterMap[KeyBit.Dots1256] = '|';
    characterMap[KeyBit.Dots246] = '{';
    characterMap[KeyBit.Dots2456] = 'w';

    characterMap[KeyBit.Dots2] = '1';
    characterMap[KeyBit.Dots23] = '2';
    characterMap[KeyBit.Dots25] = '3';
    characterMap[KeyBit.Dots256] = '4';
    characterMap[KeyBit.Dots26] = '5';
    characterMap[KeyBit.Dots235] = '6';
    characterMap[KeyBit.Dots2356] = '7';
    characterMap[KeyBit.Dots236] = '8';
    characterMap[KeyBit.Dots35] = '9';
    characterMap[KeyBit.Dots356] = '0';

    characterMap[KeyBit.Dots34] = '/';
    characterMap[KeyBit.Dots345] = '>';
    characterMap[KeyBit.Dots3456] = '#';
    characterMap[KeyBit.Dots346] = '+';
    characterMap[KeyBit.Dots3] = '\'';
    characterMap[KeyBit.Dots36] = '-';

    characterMap[KeyBit.Dots4] = '`';
    characterMap[KeyBit.Dots45] = '~';
    characterMap[KeyBit.Dots456] = '_';
    characterMap[KeyBit.Dots5] = '"';
    characterMap[KeyBit.Dots46] = '.';
    characterMap[KeyBit.Dots56] = ';';
    characterMap[KeyBit.Dots6] = ',';

    characterMap[KeyBit.Dots17] = 'A';
    characterMap[KeyBit.Dots127] = 'B';
    characterMap[KeyBit.Dots147] = 'C';
    characterMap[KeyBit.Dots1457] = 'D';
    characterMap[KeyBit.Dots157] = 'E';
    characterMap[KeyBit.Dots1247] = 'F';
    characterMap[KeyBit.Dots12457] = 'G';
    characterMap[KeyBit.Dots1257] = 'H';
    characterMap[KeyBit.Dots247] = 'I';
    characterMap[KeyBit.Dots2457] = 'J';

    characterMap[KeyBit.Dots137] = 'K';
    characterMap[KeyBit.Dots1237] = 'L';
    characterMap[KeyBit.Dots1347] = 'M';
    characterMap[KeyBit.Dots13457] = 'N';
    characterMap[KeyBit.Dots1357] = 'O';
    characterMap[KeyBit.Dots12347] = 'P';
    characterMap[KeyBit.Dots123457] = 'Q';
    characterMap[KeyBit.Dots12357] = 'R';
    characterMap[KeyBit.Dots2347] = 'S';
    characterMap[KeyBit.Dots23457] = 'T';

    characterMap[KeyBit.Dots1367] = 'U';
    characterMap[KeyBit.Dots12367] = 'V';
    characterMap[KeyBit.Dots13467] = 'X';
    characterMap[KeyBit.Dots134567] = 'Y';
    characterMap[KeyBit.Dots13567] = 'Z';

    characterMap[KeyBit.Dots124567] = ']';
    characterMap[KeyBit.Dots12567] = '\\';
    characterMap[KeyBit.Dots2467] = '[';
    characterMap[KeyBit.Dots24567] = 'W';
    characterMap[KeyBit.Dots47] = '@';
    characterMap[KeyBit.Dots457] = '^';
  }

  static {
    fillCharacterMap();
  }
}
