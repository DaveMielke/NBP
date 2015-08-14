package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperOff extends PreviousValueAction {
  public DeveloperOff (Endpoint endpoint) {
    super(endpoint, Controls.getDeveloperEnabledControl(), true);
  }
}
