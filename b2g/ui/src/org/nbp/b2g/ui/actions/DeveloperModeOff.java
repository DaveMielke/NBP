package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperModeOff extends PreviousValueAction {
  public DeveloperModeOff (Endpoint endpoint) {
    super(endpoint, Controls.developerMode, true);
  }
}
