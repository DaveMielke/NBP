package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogActionsOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogActionsControl().nextValue();
  }

  public LogActionsOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
