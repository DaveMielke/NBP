package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ShowInputMode extends Action {
  @Override
  public boolean performAction () {
    Controls.getInputModeControl().confirmValue();
    return true;
  }

  public ShowInputMode (Endpoint endpoint) {
    super(endpoint, false);
  }
}
