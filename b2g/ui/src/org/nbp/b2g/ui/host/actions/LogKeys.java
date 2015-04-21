package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class LogKeys extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogKeysControl().nextValue();
  }

  public LogKeys (Endpoint endpoint) {
    super(endpoint, true);
  }
}
