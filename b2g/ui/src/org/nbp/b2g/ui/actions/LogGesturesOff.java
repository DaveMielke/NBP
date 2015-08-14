package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogGesturesOff extends PreviousValueAction {
  public LogGesturesOff (Endpoint endpoint) {
    super(endpoint, Controls.getLogGesturesControl(), true);
  }
}
