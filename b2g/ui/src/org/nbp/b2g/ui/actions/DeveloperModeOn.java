package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperModeOn extends NextValueAction {
  public DeveloperModeOn (Endpoint endpoint) {
    super(endpoint, Controls.getDeveloperModeControl(), false);
  }
}
