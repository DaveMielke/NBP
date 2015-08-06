package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogGesturesOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogGesturesControl().previousValue();
  }

  public LogGesturesOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
