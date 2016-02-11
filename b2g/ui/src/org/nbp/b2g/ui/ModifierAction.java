package org.nbp.b2g.ui;

import java.util.Set;
import java.util.HashSet;

import org.nbp.common.Timeout;

public abstract class ModifierAction extends Action {
  private final static Set<ModifierAction> modifierActions = new HashSet<ModifierAction>();
  private boolean modifierState = false;

  private final boolean setState (boolean newState) {
    synchronized (this) {
      boolean oldState = modifierState;
      modifierState = newState;
      return oldState;
    }
  }

  private static ModifierAction[] getModifiers () {
    synchronized (modifierActions) {
      return modifierActions.toArray(new ModifierAction[modifierActions.size()]);
    }
  }

  private static void resetModifiers () {
    boolean cancelled = false;

    for (ModifierAction modifier : getModifiers()) {
      if (modifier.setState(false)) cancelled = true;
    }

    if (cancelled) ApplicationUtilities.message(R.string.message_modifier_cancelled);
  }

  private static Timeout modifierTimeout = new Timeout(ApplicationParameters.PARTIAL_ENTRY_TIMEOUT, "modifier-timeout") {
    @Override
    public void run () {
      resetModifiers();
    }
  };

  public static void cancelModifiers () {
    modifierTimeout.cancel();
    resetModifiers();
  }

  public final boolean getState () {
    return setState(false);
  }

  @Override
  public boolean performAction () {
    modifierTimeout.cancel();
    setState(true);
    modifierTimeout.start();
    return true;
  }

  protected ModifierAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);

    synchronized (modifierActions) {
      modifierActions.add(this);
    }
  }
}
