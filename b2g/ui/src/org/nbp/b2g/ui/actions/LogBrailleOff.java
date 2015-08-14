package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogBrailleOff extends PreviousValueAction {
  public LogBrailleOff (Endpoint endpoint) {
    super(endpoint, Controls.getLogBrailleControl(), true);
  }
}
