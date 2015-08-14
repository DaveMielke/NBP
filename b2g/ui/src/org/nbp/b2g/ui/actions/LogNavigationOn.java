package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigationOn extends NextValueAction {
  public LogNavigationOn (Endpoint endpoint) {
    super(endpoint, Controls.getLogNavigationControl(), true);
  }
}
