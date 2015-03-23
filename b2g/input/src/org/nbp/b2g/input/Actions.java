package org.nbp.b2g.input;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.regex.Pattern;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.KeyEvent;

public class Actions {
  private static final String LOG_TAG = Actions.class.getName();

  private static int pressedKeyMask = 0;
  private static int activeKeyMask = 0;

  private static int parseKeys (String operand) {
    int mask = 0;
    int length = operand.length();

    for (int index=0; index<length; index+=1) {
      char character = operand.charAt(index);
      int bit;

      switch (character) {
        case '1': bit = KeyMask.DOT_1;       break;
        case '2': bit = KeyMask.DOT_2;       break;
        case '3': bit = KeyMask.DOT_3;       break;
        case '4': bit = KeyMask.DOT_4;       break;
        case '5': bit = KeyMask.DOT_5;       break;
        case '6': bit = KeyMask.DOT_6;       break;
        case '7': bit = KeyMask.DOT_7;       break;
        case '8': bit = KeyMask.DOT_8;       break;

        case 's': bit = KeyMask.SPACE;       break;
        case 'f': bit = KeyMask.FORWARD;     break;
        case 'b': bit = KeyMask.BACKWARD;    break;

        case 'c': bit = KeyMask.DPAD_CENTER; break;
        case 'u': bit = KeyMask.DPAD_UP;     break;
        case 'd': bit = KeyMask.DPAD_DOWN;   break;
        case 'l': bit = KeyMask.DPAD_LEFT;   break;
        case 'r': bit = KeyMask.DPAD_RIGHT;  break;

        case 'x': bit = KeyMask.SCAN_CODE;   break;

        default:
          Log.w(LOG_TAG, "invalid key: " + character);
          return 0;
      }

      if ((mask & bit) != 0) {
        Log.w(LOG_TAG, "key specified more than once: " + character);
        continue;
      }

      mask |= bit;
    }

    return mask;
  }

  private static Action parseAction (String operand) {
    String name = Actions.class.getPackage().getName() + "." + operand + "Action";

    try {
      Class c = Class.forName(name);
      Class[] types = new Class[] {};
      Constructor constructor = c.getConstructor(types);
      return (Action)constructor.newInstance();
    } catch (ClassNotFoundException exception) {
      Log.d(LOG_TAG, "class not found: " + name);
    } catch (NoSuchMethodException exception) {
      Log.d(LOG_TAG, "constructor not found: " + name);
    } catch (IllegalAccessException exception) {
      Log.d(LOG_TAG, "constructor not accessible: " + name);
    } catch (InstantiationException exception) {
      Log.d(LOG_TAG, "instantiation error: " + name, exception);
    } catch (InvocationTargetException exception) {
      Log.d(LOG_TAG, "construction error: " + name, exception);
    }

    Log.w(LOG_TAG, "invalid action: " + operand);
    return null;
  }

  public static void add (Reader reader) {
    Pattern pattern = Pattern.compile("\\s+");
    BufferedReader buf;
    Map<String, Action> actions = new HashMap<String, Action>();

    if (reader instanceof BufferedReader) {
      buf = (BufferedReader)reader;
    } else {
      buf = new BufferedReader(reader);
    }

    while (true) {
      String line;

      try {
        line = buf.readLine();
      } catch (IOException exception) {
       Log.d(LOG_TAG, "keys configuration input error", exception);
       break;
      }

      if (line == null) break;
      String[] operands = pattern.split(line);
      int index = 0;

      if (index < operands.length) {
        if (operands[index].isEmpty()) {
          index += 1;
        }
      }

      if (index == operands.length) continue;
      String operand = operands[index++];
      if (operand.charAt(0) == '#') continue;

      int keyMask = parseKeys(operand);
      if (keyMask == 0) continue;

      if (index == operands.length) {
        Log.w(LOG_TAG, "missing keys action: " + line);
        continue;
      }

      operand = operands[index++];
      Action action = actions.get(operand);

      if (action == null) {
        action = parseAction(operand);
        if (action == null) continue;
        actions.put(operand, action);
      }

      Action.add(keyMask, action);
      if (index == operands.length) continue;

      operand = operands[index++];
      if (!action.parseOperand(keyMask, operand)) continue;
      if (index == operands.length) continue;

      Log.w(LOG_TAG, "too many operands: " + line);
    }

    try {
      reader.close();
    } catch (IOException exception) {
      Log.w(LOG_TAG, "keys configuration close error", exception);
    }
  }

  public static void add (InputStream stream) {
    Reader reader = new InputStreamReader(stream);
    add(reader);
  }

  public static void add (String asset) {
    Context context = ApplicationHooks.getContext();

    if (context != null) {
      AssetManager assets = context.getAssets();

      if (assets != null) {
        try {
          InputStream stream = assets.open(asset);

          try {
            add(stream);
          } finally {
            stream.close();
          }
        } catch (IOException exception) {
          Log.w(LOG_TAG, "asset not found: " + asset);
        }
      }
    }
  }

  public static void add () {
    Log.d(LOG_TAG, "begin key binding definitions");
    Action.add(KeyMask.VOLUME_DOWN, new VolumeDownAction());
    Action.add(KeyMask.VOLUME_UP, new VolumeUpAction());
    add("keys.conf");
    Log.d(LOG_TAG, "end key binding definitions");
  }

  private static boolean performAction (Action action) {
    if (ApplicationParameters.LOG_PERFORMED_ACTIONS) {
      Log.d(LOG_TAG, "performing action: " + action.getName());
    }

    try {
      if (action.performAction()) return true;
      Log.w(LOG_TAG, "action failed: " + action.getName());
      ApplicationUtilities.beep();
    } catch (Exception exception) {
      Log.w(LOG_TAG, "action crashed: " + action.getName(), exception);
    }

    return false;
  }

  public static int getKeyMask () {
    return activeKeyMask;
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

        if (action != null) {
          performAction(action);
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
