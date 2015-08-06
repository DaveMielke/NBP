package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogNavigationOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogNavigationControl().nextValue();
  }

  public LogNavigationOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
