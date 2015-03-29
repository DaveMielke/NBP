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

public class KeyBindings extends Action {
  private static final String LOG_TAG = KeyBindings.class.getName();

  private final static Map<String, Action> actionObjects = new HashMap<String, Action>();
  private final static Map<Integer, Action> rootKeyBindings = new HashMap<Integer, Action>();
  private static Map<Integer, Action> currentKeyBindings = rootKeyBindings;
  private Map<Integer, Action> keyBindings = new HashMap<Integer, Action>();

  @Override
  public final boolean performAction () {
    currentKeyBindings = keyBindings;
    return true;
  }

  public static void resetKeyBindings () {
    currentKeyBindings = rootKeyBindings;
  }

  private static boolean isKeyBindingsAction (Action action) {
    return action instanceof KeyBindings;
  }

  public static Action getAction (int keyMask) {
    Action action = currentKeyBindings.get(keyMask);
    boolean reset = true;

    if (action != null) {
      if (isKeyBindingsAction(action)) {
        reset = false;
      }
    }

    if (reset) resetKeyBindings();
    return action;
  }

  public static boolean addKeyBinding (int[] keyMasks, Action action) {
    Map<Integer, Action> bindings = rootKeyBindings;
    int last = keyMasks.length - 1;

    for (int index=0; index<last; index+=1) {
      int keyMask = keyMasks[index];
      Action a = bindings.get(keyMask);

      if (a == null) {
        a = new KeyBindings();
        bindings.put(keyMask, a);
      } else if (!isKeyBindingsAction(a)) {
        Log.w(LOG_TAG, "key combination already defined");
        return false;
      }

      bindings = ((KeyBindings)a).keyBindings;
    }

    {
      int keyMask = keyMasks[last];

      if (bindings.get(keyMask) != null) {
        Log.w(LOG_TAG, "key combination already defined");
        return false;
      }

      bindings.put(keyMask, action);
    }

    return true;
  }

  private static boolean addKeyBinding (int[] keyMasks, String actionName) {
    Action action = getAction(actionName);

    if (action != null) {
      if (addKeyBinding(keyMasks, action)) {
        return true;
      }
    }

    return false;
  }

  private static boolean addKeyBinding (int keyMask, String actionName) {
    int[] keyMasks = new int[] {keyMask};
    return addKeyBinding(keyMasks, actionName);
  }

  private static Action newAction (String actionName) {
    String className = KeyBindings.class.getPackage().getName() + ".actions." + actionName;

    try {
      Class classObject = Class.forName(className);
      Class[] argumentTypes = new Class[] {};
      Constructor constructor = classObject.getConstructor(argumentTypes);
      return (Action)constructor.newInstance();
    } catch (ClassNotFoundException exception) {
      Log.d(LOG_TAG, "class not found: " + className);
    } catch (NoSuchMethodException exception) {
      Log.d(LOG_TAG, "constructor not found: " + className);
    } catch (IllegalAccessException exception) {
      Log.d(LOG_TAG, "constructor not accessible: " + className);
    } catch (InstantiationException exception) {
      Log.d(LOG_TAG, "instantiation error: " + className, exception);
    } catch (InvocationTargetException exception) {
      Log.d(LOG_TAG, "construction error: " + className, exception);
    }

    Log.w(LOG_TAG, "invalid action: " + actionName);
    return null;
  }

  private static Action getAction (String actionName) {
    Action action = actionObjects.get(actionName);
    if (action != null) return action;

    action = newAction(actionName);
    if (action == null) return null;

    actionObjects.put(actionName, action);
    return action;
  }

  private static int[] addKeyMask (int[] oldMasks, int mask) {
    int[] newMasks;

    if (mask == 0) {
      Log.w(LOG_TAG, "missing key combination");
      return null;
    }

    if (oldMasks == null) {
      newMasks = new int[1];
    } else {
      newMasks = new int[oldMasks.length + 1];
      System.arraycopy(oldMasks, 0, newMasks, 0, oldMasks.length);
    }

    newMasks[newMasks.length - 1] = mask;
    return newMasks;
  }

  private static int[] parseKeys (String operand) {
    int length = operand.length();
    int[] masks = null;
    int mask = 0;

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

        case '-':
          masks = addKeyMask(masks, mask);
          if (masks == null) return null;
          mask = 0;
          continue;

        default:
          Log.w(LOG_TAG, "invalid key: " + character);
          return null;
      }

      if ((mask & bit) != 0) {
        Log.w(LOG_TAG, "key specified more than once: " + character);
        continue;
      }

      mask |= bit;
    }

    return addKeyMask(masks, mask);
  }

  private static void addKeyBindings (Reader reader) {
    Pattern pattern = Pattern.compile("\\s+");
    BufferedReader buf;

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

      int[] keyMasks = parseKeys(operand);
      if (keyMasks == null) continue;
      int keyMask = keyMasks[keyMasks.length - 1];

      if (index == operands.length) {
        Log.w(LOG_TAG, "missing keys action: " + line);
        continue;
      }

      operand = operands[index++];
      Action action = getAction(operand);
      if (action == null) continue;
      addKeyBinding(keyMasks, action);

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

  private static void addKeyBindings (InputStream stream) {
    Reader reader = new InputStreamReader(stream);
    addKeyBindings(reader);
  }

  private static void addKeyBindings (String asset) {
    Context context = ApplicationHooks.getContext();

    if (context != null) {
      AssetManager assets = context.getAssets();

      if (assets != null) {
        try {
          InputStream stream = assets.open(asset);

          try {
            addKeyBindings(stream);
          } finally {
            stream.close();
          }
        } catch (IOException exception) {
          Log.w(LOG_TAG, "asset not found: " + asset);
        }
      }
    }
  }

  private static void addKeyBindings () {
    Log.d(LOG_TAG, "begin key binding definitions");
    addKeyBinding(KeyMask.VOLUME_DOWN, "VolumeDown");
    addKeyBinding(KeyMask.VOLUME_UP, "VolumeUp");
    addKeyBindings("keys.conf");
    Log.d(LOG_TAG, "end key binding definitions");
  }

  private KeyBindings () {
    super();
  }

  public static void load () {
  }

  static {
    addKeyBindings();
  }
}
