package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputModeText extends PreviousValueAction {
  public InputModeText (Endpoint endpoint) {
    super(endpoint, Controls.getInputModeControl(), false);
  }
}
