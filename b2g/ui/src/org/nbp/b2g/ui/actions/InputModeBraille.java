package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputModeBraille extends NextValueAction {
  public InputModeBraille (Endpoint endpoint) {
    super(endpoint, Controls.getInputModeControl(), false);
  }
}
