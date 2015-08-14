package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class DeveloperOn extends NextValueAction {
  public DeveloperOn (Endpoint endpoint) {
    super(endpoint, Controls.getDeveloperEnabledControl(), false);
  }
}
