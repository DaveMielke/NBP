package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputBoldOff extends PreviousValueAction {
  public InputBoldOff (Endpoint endpoint) {
    super(endpoint, Controls.getInputBoldControl(), false);
  }
}
