package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigationOff extends PreviousValueAction {
  public LogNavigationOff (Endpoint endpoint) {
    super(endpoint, Controls.logNavigation, true);
  }
}
