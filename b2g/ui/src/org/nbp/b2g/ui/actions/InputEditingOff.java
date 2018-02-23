package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InputEditingOff extends PreviousValueAction {
  public InputEditingOff (Endpoint endpoint) {
    super(endpoint, Controls.inputEditing, false);
  }
}
