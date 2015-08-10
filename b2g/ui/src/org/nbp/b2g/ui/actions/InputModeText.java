package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputModeText extends Action {
  @Override
  public boolean performAction () {
    return Controls.getInputModeControl().previousValue();
  }

  public InputModeText (Endpoint endpoint) {
    super(endpoint, false);
  }
}
