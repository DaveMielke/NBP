package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ControlModifier extends ModifierAction {
  @Override
  protected final int getModifierLabel () {
    return R.string.message_modifier_control;
  }

  public ControlModifier (Endpoint endpoint) {
    super(endpoint, false);
  }
}
