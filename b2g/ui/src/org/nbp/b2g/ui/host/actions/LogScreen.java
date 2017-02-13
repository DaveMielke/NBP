package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import org.nbp.common.LogLogger;

public class LogScreen extends Action {
  @Override
  public boolean performAction () {
    return new LogLogger("screen-log", new NodeIterator()).log();
  }

  @Override
  protected Integer getConfirmation () {
    return R.string.LogScreen_action_confirmation;
  }

  public LogScreen (Endpoint endpoint) {
    super(endpoint, true);
  }
}
