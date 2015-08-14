package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputModeConfirm extends ConfirmValueAction {
  public InputModeConfirm (Endpoint endpoint) {
    super(endpoint, Controls.getInputModeControl(), false);
  }
}
