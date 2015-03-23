package org.nbp.b2g.input;

public class ControlModifierAction extends ModifierAction {
  private static ModifierAction controlModifier = null;

  public static ModifierAction getControlModifier () {
    return controlModifier;
  }

  public ControlModifierAction () {
    super();
    controlModifier = this;
  }
}
