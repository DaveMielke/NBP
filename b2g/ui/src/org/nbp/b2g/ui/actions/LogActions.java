package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogActions extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogActionsControl().nextValue();
  }

  public LogActions (Endpoint endpoint) {
    super(endpoint, true);
  }
}
