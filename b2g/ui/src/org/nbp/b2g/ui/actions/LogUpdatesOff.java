package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogUpdatesOff extends PreviousValueAction {
  public LogUpdatesOff (Endpoint endpoint) {
    super(endpoint, Controls.logUpdates, true);
  }
}
