package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogBrailleOn extends NextValueAction {
  public LogBrailleOn (Endpoint endpoint) {
    super(endpoint, Controls.getLogBrailleControl(), true);
  }
}
