package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputUnderlineOff extends PreviousValueAction {
  public InputUnderlineOff (Endpoint endpoint) {
    super(endpoint, Controls.getInputUnderlineControl(), false);
  }
}
