package org.nbp.b2g.input.actions;
import org.nbp.b2g.input.*;

public class ControlModifier extends ModifierAction {
  private static ModifierAction controlModifier = null;

  public static ModifierAction getControlModifier () {
    return controlModifier;
  }

  public ControlModifier () {
    super();
    controlModifier = this;
  }
}
