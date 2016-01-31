package org.nbp.b2g.ui;

public abstract class InputAction extends Action {
  protected abstract boolean performInputAction ();

  @Override
  public boolean performAction () {
    if (!getEndpoint().isInputArea()) return false;
    return performInputAction();
  }

  protected InputAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
