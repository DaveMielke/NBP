package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogGesturesOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogGesturesControl().nextValue();
  }

  public LogGesturesOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
