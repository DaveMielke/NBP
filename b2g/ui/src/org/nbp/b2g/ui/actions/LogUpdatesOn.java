package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogUpdatesOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogUpdatesControl().nextValue();
  }

  public LogUpdatesOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
