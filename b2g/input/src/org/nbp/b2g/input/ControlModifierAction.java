package org.nbp.b2g.input;

public class ControlModifierAction extends ToggleAction {
  private static ToggleAction controlModifier = null;

  public static ToggleAction getControlModifier () {
    return controlModifier;
  }

  public ControlModifierAction () {
    super();
    controlModifier = this;
  }
}
