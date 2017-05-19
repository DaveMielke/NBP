package org.nbp.b2g.ui;

public abstract class PreviousValueAction extends ControlAction<Control> {
  @Override
  public boolean performAction () {
    return getControl().previousValue();
  }

  protected PreviousValueAction (Endpoint endpoint, Control control, boolean isAdvanced) {
    super(endpoint, control, isAdvanced);
  }
}
