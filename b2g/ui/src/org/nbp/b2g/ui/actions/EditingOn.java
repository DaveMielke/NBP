package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class EditingOn extends NextValueAction {
  public EditingOn (Endpoint endpoint) {
    super(endpoint, Controls.editingEnabled, false);
  }
}
