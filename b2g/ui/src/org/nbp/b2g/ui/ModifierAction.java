package org.nbp.b2g.ui;

public abstract class ModifierAction extends Action {
  private boolean modifierState = false;

  public boolean getState () {
    boolean state = modifierState;
    modifierState = false;
    return state;
  }

  @Override
  public boolean performAction () {
    modifierState = true;
    return true;
  }

  protected ModifierAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
