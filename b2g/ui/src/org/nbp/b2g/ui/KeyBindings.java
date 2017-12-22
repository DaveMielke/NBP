package org.nbp.b2g.ui;

import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.Pattern;

import org.nbp.common.LanguageUtilities;
import org.nbp.common.InputProcessor;
import org.nbp.common.DirectiveProcessor;
import org.nbp.common.Timeout;

import android.util.Log;

public class KeyBindings {
  private final static String LOG_TAG = KeyBindings.class.getName();

  private final static Pattern KEY_COMBINATION_PATTERN = Pattern.compile(
    "\\" + KeyMask.KEY_COMBINATION_DELIMITER
  );

  private final static Pattern KEY_NAME_PATTERN = Pattern.compile(
    "\\" + KeyMask.KEY_NAME_DELIMITER
  );

  private final Endpoint endpoint;

  private final Map<String, Class<? extends Action>> actionNameCache = new HashMap<String, Class<? extends Action>>();
  private final Map<Class<? extends Action>, Action> actionClassCache = new HashMap<Class<? extends Action>, Action>();

  private final KeyBindingMap rootKeyBindings = new KeyBindingMap();
  private KeyBindingMap currentKeyBindings = rootKeyBindings;

  public final KeyBindingMap getRootKeyBindingMap () {
    return rootKeyBindings;
  }

  private boolean setKeyBindings (KeyBindingMap keyBindings) {
    synchronized (rootKeyBindings) {
      if (currentKeyBindings == keyBindings) return false;

      currentKeyBindings = keyBindings;
      return true;
    }
  }

  private boolean setKeyBindings () {
    return setKeyBindings(rootKeyBindings);
  }

  private final Timeout partialEntryTimeout = new Timeout(ApplicationParameters.PARTIAL_ENTRY_TIMEOUT, "partial-entry-timeout") {
    @Override
    public void run () {
      try {
        ActionChooser.chooseAction(currentKeyBindings);
      } finally {
        setKeyBindings();
      }
    }
  };

  public void resetKeyBindings () {
    partialEntryTimeout.cancel();
    setKeyBindings();
  }

  private class PartialEntry extends Action {
    public final KeyBindingMap keyBindings = new KeyBindingMap();

    @Override
    public boolean performAction () {
      partialEntryTimeout.cancel();
      setKeyBindings(keyBindings);
      partialEntryTimeout.start();
      return true;
    }

    @Override
    public String getSummary () {
      return ApplicationContext.getString(R.string.message_partial_entry_summary);
    }

    @Override
    protected Integer getConfirmation () {
      return R.string.message_partial_entry_initiated;
    }

    public PartialEntry (Endpoint endpoint) {
      super(endpoint, false);
    }
  }

  public boolean isRootKeyBindings () {
    return currentKeyBindings == rootKeyBindings;
  }

  private static boolean isPartialEntry (Action action) {
    return action instanceof PartialEntry;
  }

  public Action getAction (int keyMask) {
    Action action = currentKeyBindings.get(keyMask);
    boolean reset = true;

    if (action != null) {
      if (isPartialEntry(action)) {
        reset = false;
      }
    }

    if (reset) resetKeyBindings();
    if (action == null) return null;
    if (!action.isAdvanced()) return action;
    if (ApplicationSettings.ADVANCED_ACTIONS) return action;
    return null;
  }

  public boolean addKeyBinding (Action action, int... keyMasks) {
    KeyBindingMap bindings = rootKeyBindings;
    int last = keyMasks.length - 1;

    for (int index=0; index<last; index+=1) {
      int keyMask = keyMasks[index];
      Action partialEntry = bindings.get(keyMask);

      if (partialEntry == null) {
        partialEntry = new PartialEntry(endpoint);
        bindings.put(keyMask, partialEntry);
      } else if (!isPartialEntry(partialEntry)) {
        return false;
      }

      bindings = ((PartialEntry)partialEntry).keyBindings;
    }

    {
      int keyMask = keyMasks[last];
      if (bindings.get(keyMask) != null) return false;
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
    if (type == null) return null;

    {
      Action action = actionClassCache.get(type);
      if (action != null) return action;
    }

    {
      Action action = newAction(type);
      if (action == null) return null;

      actionClassCache.put(type, action);
      return action;
    }
  }

  private Class<? extends Action> getActionClass (String name, Object owner) {
    String className = owner.getClass().getPackage().getName() + ".actions." + name;

    try {
      Class type = Class.forName(className);
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

  private static int[] parseKeyCombination (String operand) {
    String[] combinations = KEY_COMBINATION_PATTERN.split(operand);
    int[] masks = null;

    for (String combination : combinations) {
      if (combination.isEmpty()) {
        Log.w(LOG_TAG, "missing key combination: " + operand);
        return null;
      }

      String[] names = KEY_NAME_PATTERN.split(combination);
      int mask = 0;

      for (String name : names) {
        {
          int comment = name.indexOf('#');
          if (comment >= 0) name = name.substring(0, comment);
        }

        if (name.isEmpty()) {
          Log.w(LOG_TAG, "missing key name: " + combination);
          return null;
        }

        Integer bit = KeyMask.toBit(name);
        if (bit == null) return null;

        if ((mask & bit) != 0) {
          Log.w(LOG_TAG, "key specified more than once: " + name);
          return null;
        }

        mask |= bit;
      }

      masks = addKeyMask(masks, mask);
      if (masks == null) return null;
    }

    return masks;
  }

  private boolean bindKeyCombination (String[] operands) {
    int index = 0;

    if (index == operands.length) {
      Log.w(LOG_TAG, "key combination not specified");
      return true;
    }

    String keyCombination = operands[index++];
    int[] keyMasks = parseKeyCombination(keyCombination);
    if (keyMasks == null) return true;

    if (index == operands.length) {
      Log.w(LOG_TAG, "action not specified");
      return true;
    }

    String actionName = operands[index++];
    Action action = getAction(actionName);
    if (action == null) return true;

    if (index < operands.length) {
      Log.w(LOG_TAG, "too many operands");
    }

    if (!addKeyBinding(action, keyMasks)) {
      Log.w(LOG_TAG, "key combination already bound: " + keyCombination);
      return true;
    }

    return true;
  }

  private InputProcessor makeInputProcessor () {
    return new DirectiveProcessor()
      .addDirective("bind",
        new DirectiveProcessor.DirectiveHandler() {
          @Override
          public boolean handleDirective (String[] operands) {
            return bindKeyCombination(operands);
          }
        }
      )

      .setSkipCommentLines(true)
      .setTrimTrailingComments(true)
      ;
  }

  private void addKeyBindings (String name) {
    Log.d(LOG_TAG, "begin key binding definitions: " + name);
    makeInputProcessor().processInput((name + ".keys"));
    Log.d(LOG_TAG, "end key binding definitions: " + name);
  }

  public KeyBindings (Endpoint endpoint, String name) {
    this.endpoint = endpoint;
    addKeyBindings(name);
  }
}
