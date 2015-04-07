package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;

import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.regex.Pattern;

public class KeyBindings {
  private final static String LOG_TAG = KeyBindings.class.getName();

  public final static char DELIMITER = '-';
  public final static char DOT_1     = '1';
  public final static char DOT_2     = '2';
  public final static char DOT_3     = '3';
  public final static char DOT_4     = '4';
  public final static char DOT_5     = '5';
  public final static char DOT_6     = '6';
  public final static char DOT_7     = '7';
  public final static char DOT_8     = '8';
  public final static char SPACE     = 's';
  public final static char FORWARD   = 'f';
  public final static char BACKWARD  = 'b';
  public final static char CENTER    = 'c';
  public final static char UP        = 'u';
  public final static char DOWN      = 'd';
  public final static char LEFT      = 'l';
  public final static char RIGHT     = 'r';
  public final static char CURSOR    = 'x';

  private final Map<String, Action> actionObjects = new HashMap<String, Action>();
  private final KeyBindingMap rootKeyBindings = new KeyBindingMap();
  private KeyBindingMap currentKeyBindings = rootKeyBindings;

  private class IntermediateAction extends Action {
    public final KeyBindingMap keyBindings = new KeyBindingMap();

    @Override
    public boolean performAction () {
      currentKeyBindings = keyBindings;
      return true;
    }

    public IntermediateAction () {
      super(false);
    }
  }

  public void resetKeyBindings () {
    currentKeyBindings = rootKeyBindings;
  }

  private static boolean isIntermediateAction (Action action) {
    return action instanceof IntermediateAction;
  }

  public Action getAction (int keyMask) {
    Action action = currentKeyBindings.get(keyMask);
    boolean reset = true;

    if (action != null) {
      if (isIntermediateAction(action)) {
        reset = false;
      }
    }

    if (reset) resetKeyBindings();
    if (action == null) return null;
    if (!action.isForDevelopers()) return action;
    if (ApplicationParameters.ENABLE_DEVELOPER_ACTIONS) return action;
    return null;
  }

  public boolean addKeyBinding (int[] keyMasks, Action action) {
    KeyBindingMap bindings = rootKeyBindings;
    int last = keyMasks.length - 1;

    for (int index=0; index<last; index+=1) {
      int keyMask = keyMasks[index];
      Action intermediate = bindings.get(keyMask);

      if (intermediate == null) {
        intermediate = new IntermediateAction();
        bindings.put(keyMask, intermediate);
      } else if (!isIntermediateAction(intermediate)) {
        Log.w(LOG_TAG, "key combination already defined");
        return false;
      }

      bindings = ((IntermediateAction)intermediate).keyBindings;
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

  private boolean addKeyBinding (int[] keyMasks, String actionName) {
    Action action = getAction(actionName);

    if (action != null) {
      if (addKeyBinding(keyMasks, action)) {
        return true;
      }
    }

    return false;
  }

  private boolean addKeyBinding (int keyMask, String actionName) {
    int[] keyMasks = new int[] {keyMask};
    return addKeyBinding(keyMasks, actionName);
  }

  private Action newAction (String actionName) {
    String className = KeyBindings.class.getPackage().getName() + ".host.actions." + actionName;

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

  private Action getAction (String actionName) {
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
        case KeyBindings.DOT_1:    bit = KeyMask.DOT_1;       break;
        case KeyBindings.DOT_2:    bit = KeyMask.DOT_2;       break;
        case KeyBindings.DOT_3:    bit = KeyMask.DOT_3;       break;
        case KeyBindings.DOT_4:    bit = KeyMask.DOT_4;       break;
        case KeyBindings.DOT_5:    bit = KeyMask.DOT_5;       break;
        case KeyBindings.DOT_6:    bit = KeyMask.DOT_6;       break;
        case KeyBindings.DOT_7:    bit = KeyMask.DOT_7;       break;
        case KeyBindings.DOT_8:    bit = KeyMask.DOT_8;       break;
        case KeyBindings.SPACE:    bit = KeyMask.SPACE;       break;
        case KeyBindings.FORWARD:  bit = KeyMask.FORWARD;     break;
        case KeyBindings.BACKWARD: bit = KeyMask.BACKWARD;    break;
        case KeyBindings.CENTER:   bit = KeyMask.DPAD_CENTER; break;
        case KeyBindings.UP:       bit = KeyMask.DPAD_UP;     break;
        case KeyBindings.DOWN:     bit = KeyMask.DPAD_DOWN;   break;
        case KeyBindings.LEFT:     bit = KeyMask.DPAD_LEFT;   break;
        case KeyBindings.RIGHT:    bit = KeyMask.DPAD_RIGHT;  break;
        case KeyBindings.CURSOR:   bit = KeyMask.CURSOR;      break;

        case KeyBindings.DELIMITER:
          masks = addKeyMask(masks, mask);
          if (masks == null) return null;
          mask = 0;
          continue;

        default:
          Log.w(LOG_TAG, "invalid key: " + character);
          return null;
      }

      if ((mask & bit) != 0) {
        Log.w(LOG_TAG, "key specified more than once: " + operand);
        continue;
      }

      mask |= bit;
    }

    return addKeyMask(masks, mask);
  }

  private void addKeyBindings (Reader reader) {
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

  private void addKeyBindings (InputStream stream) {
    String encoding = "UTF8";
    Reader reader;

    try {
      reader = new InputStreamReader(stream, encoding);
      addKeyBindings(reader);
    } catch (UnsupportedEncodingException exception) {
      Log.e(LOG_TAG, "unsupported input character encoding: " + encoding);
    }
  }

  private void addKeyBindings (String asset) {
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

  public void addKeyBindings (String[] keysFileNames) {
    Log.d(LOG_TAG, "begin key binding definitions");

    addKeyBinding(KeyMask.VOLUME_DOWN, "VolumeDown");
    addKeyBinding(KeyMask.VOLUME_UP, "VolumeUp");

    if (keysFileNames != null) {
      for (String name : keysFileNames) {
        addKeyBindings((name + ".keys"));
      }
    }

    Log.d(LOG_TAG, "end key binding definitions");
  }

  public KeyBindings () {
  }
}
