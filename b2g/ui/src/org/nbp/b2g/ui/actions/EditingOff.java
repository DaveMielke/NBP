package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class EditingOff extends PreviousValueAction {
  public EditingOff (Endpoint endpoint) {
    super(endpoint, Controls.editingEnabled, false);
  }
}
