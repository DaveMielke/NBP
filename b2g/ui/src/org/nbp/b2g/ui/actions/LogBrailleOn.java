package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogBrailleOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogBrailleControl().nextValue();
  }

  public LogBrailleOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
