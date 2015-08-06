package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogUpdatesOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogUpdatesControl().previousValue();
  }

  public LogUpdatesOff (Endpoint endpoint) {
    super(endpoint, true);
  }
}
