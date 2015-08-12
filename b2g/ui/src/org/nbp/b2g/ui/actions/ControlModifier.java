package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ControlModifier extends ModifierAction {
  @Override
  protected Integer getConfirmation () {
    return R.string.ControlModifier_action_confirmation;
  }

  public ControlModifier (Endpoint endpoint) {
    super(endpoint, false);
  }
}
