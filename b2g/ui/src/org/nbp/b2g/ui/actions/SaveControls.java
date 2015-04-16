package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SaveControls extends Action {
  @Override
  public boolean performAction () {
    Controls.saveControls();
    message("controls saved");
    return true;
  }

  public SaveControls (Endpoint endpoint) {
    super(endpoint, true);
  }
}
