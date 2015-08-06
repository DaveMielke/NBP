package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogBrailleOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogBrailleControl().previousValue();
  }

  public LogBrailleOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
