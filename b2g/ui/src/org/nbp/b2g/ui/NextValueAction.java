package org.nbp.b2g.ui;

public abstract class NextValueAction extends ControlAction<Control> {
  @Override
  public boolean performAction () {
    return getControl().nextValue();
  }

  protected NextValueAction (Endpoint endpoint, Control control, boolean isAdvanced) {
    super(endpoint, control, isAdvanced);
  }
}
