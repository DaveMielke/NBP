package org.nbp.b2g.ui;

import java.util.Set;
import java.util.HashSet;

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

  public static void cancelModifiers () {
    boolean cancelled = false;

    for (ModifierAction modifier : getModifiers()) {
      if (modifier.setState(false)) cancelled = true;
    }

    if (cancelled) ApplicationUtilities.message(R.string.message_modifier_cancelled);
  }

  public final boolean getState () {
    return setState(false);
  }

  @Override
  public boolean performAction () {
    setState(true);
    return true;
  }

  protected ModifierAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);

    synchronized (modifierActions) {
      modifierActions.add(this);
    }
  }
}
