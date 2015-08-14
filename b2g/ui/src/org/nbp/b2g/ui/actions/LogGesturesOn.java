package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogGesturesOn extends NextValueAction {
  public LogGesturesOn (Endpoint endpoint) {
    super(endpoint, Controls.getLogGesturesControl(), true);
  }
}
