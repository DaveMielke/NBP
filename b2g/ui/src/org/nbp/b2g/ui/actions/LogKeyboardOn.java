package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LogKeyboardOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLogKeyboardControl().nextValue();
  }

  public LogKeyboardOn (Endpoint endpoint) {
    super(endpoint, true);
  }
}
