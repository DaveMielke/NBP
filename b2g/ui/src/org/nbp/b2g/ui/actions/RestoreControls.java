package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class RestoreControls extends Action {
  @Override
  public boolean performAction () {
    Controls.restoreControls();
    message("controls restored");
    return true;
  }

  public RestoreControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
