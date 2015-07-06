package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogUpdates extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogUpdatesControl().nextValue();
  }

  public LogUpdates (Endpoint endpoint) {
    super(endpoint, true);
  }
}
