package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class TypeControl extends ModifierAction {
  @Override
  protected Integer getConfirmation () {
    return R.string.TypeControl_action_confirmation;
  }

  @Override
  public boolean editsInput () {
    return true;
  }

  public TypeControl (Endpoint endpoint) {
    super(endpoint, false);
  }
}
