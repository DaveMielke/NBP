package org.nbp.b2g.ui;

public abstract class ControlAction<C extends Control> extends Action {
  private final C control;

  protected final C getControl () {
    return control;
  }

  protected ControlAction (Endpoint endpoint, C control, boolean isAdvanced) {
    super(endpoint, isAdvanced);
    this.control = control;
  }
}
