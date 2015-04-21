package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LongPressOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLongPressControl().previousValue();
  }

  public LongPressOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
