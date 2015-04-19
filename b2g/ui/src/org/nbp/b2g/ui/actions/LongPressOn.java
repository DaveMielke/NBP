package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class LongPressOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getLongPressControl().setNextValue();
  }

  public LongPressOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
