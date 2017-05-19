package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogUpdatesOn extends NextValueAction {
  public LogUpdatesOn (Endpoint endpoint) {
    super(endpoint, Controls.logUpdates, true);
  }
}
