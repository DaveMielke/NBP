package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.regex.Pattern;

public class KeyBindings {
  private final static String LOG_TAG = KeyBindings.class.getName();

  public final static char KEY_COMBINATION_DELIMITER = '-';

  private final Endpoint endpoint;

  private final Map<String, Class<? extends Action>> actionNameCache = new HashMap<String, Class<? extends Action>>();
  private final Map<Class<? extends Action>, Action> actionClassCache = new HashMap<Class<? extends Action>, Action>();

  private final KeyBindingMap rootKeyBindings = new KeyBindingMap();
  private KeyBindingMap currentKeyBindings = rootKeyBindings;

  private class IntermediateAction extends Action {
    public final KeyBindingMap keyBindings = new KeyBindingMap();

    @Override
    public boolean performAction () {
      currentKeyBindings = keyBindings;
      return true;
    }

    public IntermediateAction (Endpoint endpoint) {
      super(endpoint, false);
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
    if (ApplicationSettings.DEVELOPER_ACTIONS) return action;
    return null;
  }

  public boolean addKeyBinding (Action action, int... keyMasks) {
    KeyBindingMap bindings = rootKeyBindings;
    int last = keyMasks.length - 1;

    for (int index=0; index<last; index+=1) {
      int keyMask = keyMasks[index];
      Action intermediate = bindings.get(keyMask);

      if (intermediate == null) {
        intermediate = new IntermediateAction(endpoint);
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

  private Action newAction (Class<? extends Action> type) {
    String name = type.getName();

    try {
      Class[] arguments = new Class[] {Endpoint.class};
      Constructor constructor = type.getConstructor(arguments);
      return (Action)constructor.newInstance(endpoint);
    } catch (NoSuchMethodException exception) {
      Log.w(LOG_TAG, "constructor not found: " + name);
    } catch (IllegalAccessException exception) {
      Log.w(LOG_TAG, "constructor not accessible: " + name);
    } catch (InstantiationException exception) {
      Log.w(LOG_TAG, "instantiation error: " + name, exception);
    } catch (InvocationTargetException exception) {
      Log.w(LOG_TAG, "construction error: " + name, exception);
    }

    return null;
  }

  public Action getAction (Class<? extends Action> type) {
    Action action;
    if ((action = actionClassCache.get(type)) != null) return action;

    if ((action = newAction(type)) != null) actionClassCache.put(type, action);
    return action;
  }

  private Class<? extends Action> getActionClass (String name, Object owner) {
    try {
      Class type = Class.forName(
        (owner.getClass().getPackage().getName() + ".actions." + name)
      );

      if (LanguageUtilities.canAssign(Action.class, type)) return type;
      Log.w(LOG_TAG, "not an action: " + type.getName());
    } catch (ClassNotFoundException exception) {
    }

    return null;
  }

  private Class<? extends Action> getActionClass (String name) {
    Class<? extends Action> type;

    if ((type = getActionClass(name, endpoint)) != null) return type;
    if ((type = getActionClass(name, this)) != null) return type;

    Log.w(LOG_TAG, "unknown action: " + name);
    return null;
  }

  private Action getAction (String name) {
    Class<? extends Action> type = actionNameCache.get(name);

    if (type == null) {
      if ((type = getActionClass(name)) == null) return null;
      actionNameCache.put(name, type);
    }

    return getAction(type);
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

      if (character == KEY_COMBINATION_DELIMITER) {
        masks = addKeyMask(masks, mask);
        if (masks == null) return null;
        mask = 0;
      } else {
        Integer bit = KeyMask.toBit(Character.toUpperCase(character));

        if (bit == null) {
          Log.w(LOG_TAG, "invalid key: " + character);
          return null;
        }

        if ((mask & bit) != 0) {
          Log.w(LOG_TAG, "key specified more than once: " + operand);
          return null;
        }

        mask |= bit;
      }
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
      addKeyBinding(action, keyMasks);

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

    try {
      Reader reader = new InputStreamReader(stream, encoding);
      addKeyBindings(reader);
    } catch (UnsupportedEncodingException exception) {
      Log.e(LOG_TAG, "unsupported input character encoding: " + encoding);
    }
  }

  private void addKeyBindings (String asset) {
    Context context = ApplicationContext.getContext();

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

  private void addKeyBindings (String[] keysFileNames) {
    String endpointName = LanguageUtilities.getClassName(endpoint.getClass());
    Log.d(LOG_TAG, "begin key binding definitions: " + endpointName);

    if (keysFileNames != null) {
      for (String keysFileName : keysFileNames) {
        addKeyBindings((keysFileName + ".keys"));
      }
    }

    Log.d(LOG_TAG, "end key binding definitions: " + endpointName);
  }

  public KeyBindings (Endpoint endpoint, String[] keysFileNames) {
    this.endpoint = endpoint;
    addKeyBindings(keysFileNames);
  }
}
