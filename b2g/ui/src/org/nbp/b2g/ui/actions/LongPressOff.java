package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LongPressOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLongPressControl().setPreviousValue();
  }

  public LongPressOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
