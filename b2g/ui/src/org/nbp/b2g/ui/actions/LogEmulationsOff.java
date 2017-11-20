package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogEmulationsOff extends PreviousValueAction {
  public LogEmulationsOff (Endpoint endpoint) {
    super(endpoint, Controls.logEmulations, true);
  }
}
