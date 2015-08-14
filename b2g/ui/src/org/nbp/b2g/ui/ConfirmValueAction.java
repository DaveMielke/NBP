package org.nbp.b2g.ui;

public abstract class ConfirmValueAction extends ControlAction<Control> {
  @Override
  public boolean performAction () {
    getControl().confirmValue();
    return true;
  }

  protected ConfirmValueAction (Endpoint endpoint, Control control, boolean isForDevelopers) {
    super(endpoint, control, isForDevelopers);
  }
}
