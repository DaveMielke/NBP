package org.nbp.b2g.ui;

public abstract class InputAction extends Action {
  protected abstract boolean performInputAction ();

  @Override
  public boolean performAction () {
    if (getEndpoint().isInputArea()) return performInputAction();

    Byte dots = KeyMask.toDots(getNavigationKeys());
    if (dots == null) return false;
    if (dots == 0) return false;
    return getEndpoint().handleDots(dots);
  }

  protected InputAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
