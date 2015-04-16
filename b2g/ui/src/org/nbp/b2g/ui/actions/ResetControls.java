package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ResetControls extends Action {
  @Override
  public boolean performAction () {
    Controls.resetControls();
    message("controls reset");
    return true;
  }

  public ResetControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
