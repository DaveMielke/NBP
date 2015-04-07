package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class ControlModifier extends ModifierAction {
  private static ModifierAction controlModifier = null;

  public static ModifierAction getControlModifier () {
    return controlModifier;
  }

  public ControlModifier (Endpoint endpoint) {
    super(endpoint, false);
    controlModifier = this;
  }
}
