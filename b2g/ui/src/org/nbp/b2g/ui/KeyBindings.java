package org.nbp.b2g.ui;

import org.nbp.common.LanguageUtilities;
import java.lang.reflect.*;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.Pattern;

import org.nbp.common.InputProcessor;
import org.nbp.common.DirectiveProcessor;

import org.nbp.common.Timeout;
import org.nbp.common.Tones;

import android.util.Log;

public class KeyBindings {
  private final static String LOG_TAG = KeyBindings.class.getName();

  public final static char KEY_COMBINATION_DELIMITER = ',';
  public final static char KEY_NAME_DELIMITER = '+';

  private final static Pattern KEY_COMBINATION_PATTERN = Pattern.compile(
    "\\" + KEY_COMBINATION_DELIMITER
  );

  private final static Pattern KEY_NAME_PATTERN = Pattern.compile(
    "\\" + KEY_NAME_DELIMITER
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

  private final class PartialEntry extends Action {
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

  public Action getAction (KeySet keys) {
    Action action = currentKeyBindings.get(keys);
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

  private final boolean addAction (KeyBindingMap bindings, KeySet keys, Action action) {
    Action current = bindings.put(keys, action);
    if (current == null) return true;

    Log.w(LOG_TAG,
      String.format(
        "duplicate key binding: %s: %s & %s",
        keys.toString(), current.getName(), action.getName()
      )
    );

    bindings.put(keys, current);
    return false;
  }

  public boolean addKeyBinding (Action action, KeySet... keySets) {
    KeyBindingMap bindings = rootKeyBindings;
    int last = keySets.length - 1;

    for (int index=0; index<last; index+=1) {
      KeySet keySet = keySets[index];
      Action current = bindings.get(keySet);

      if (current == null) {
        current = new PartialEntry(endpoint);
        addAction(bindings, keySet, current);
      } else if (!isPartialEntry(current)) {
        return false;
      }

      bindings = ((PartialEntry)current).keyBindings;
    }

    return addAction(bindings, keySets[last], action);
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

  private static KeySet[] addKeySet (KeySet[] oldSets, KeySet set) {
    KeySet[] newSets;

    if (set == null) {
      Log.w(LOG_TAG, "missing key combination");
      return null;
    }

    if (oldSets == null) {
      newSets = new KeySet[1];
    } else {
      newSets = new KeySet[oldSets.length + 1];
      System.arraycopy(oldSets, 0, newSets, 0, oldSets.length);
    }

    newSets[newSets.length - 1] = set;
    return newSets;
  }

  private static KeySet[] parseKeyCombination (String operand) {
    String[] combinations = KEY_COMBINATION_PATTERN.split(operand);
    KeySet[] sets = null;

    for (String combination : combinations) {
      if (combination.isEmpty()) {
        Log.w(LOG_TAG, "missing key combination: " + operand);
        return null;
      }

      String[] names = KEY_NAME_PATTERN.split(combination);
      KeySet set = new KeySet();

      for (String name : names) {
        {
          int comment = name.indexOf('#');
          if (comment >= 0) name = name.substring(0, comment);
        }

        if (name.isEmpty()) {
          Log.w(LOG_TAG, "missing key name: " + combination);
          return null;
        }

        KeySet keys = KeySet.fromName(name);
        if (keys == null) return null;

        if (set.intersects(keys)) {
          Log.w(LOG_TAG, "key specified more than once: " + name);
          return null;
        }

        set.add(keys);
      }

      sets = addKeySet(sets, set.freeze());
      if (sets == null) return null;
    }

    return sets;
  }

  private boolean bindKeyCombination (String[] operands) {
    int index = 0;

    if (index == operands.length) {
      Log.w(LOG_TAG, "key combination not specified");
      return true;
    }

    String keyCombination = operands[index++];
    KeySet[] keySets = parseKeyCombination(keyCombination);
    if (keySets == null) return true;

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

    if (!addKeyBinding(action, keySets)) {
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

  public void addKeyBindings (String name) {
    Log.d(LOG_TAG, "begin key binding definitions: " + name);
    makeInputProcessor().processInput((name + ".keys"));
    Log.d(LOG_TAG, "end key binding definitions: " + name);
  }

  public KeyBindings (Endpoint endpoint) {
    this.endpoint = endpoint;
  }
}
