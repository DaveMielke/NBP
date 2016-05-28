package org.nbp.b2g.ui;

public abstract class InputAction extends Action {
  protected abstract boolean performInputAction ();

  protected boolean performNonInputAction () {
    return false;
  }

  @Override
  public boolean performAction () {
    return getEndpoint().isInputArea()? 
           performInputAction():
           performNonInputAction();
  }

  protected InputAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
