package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getBrailleEnabledControl().previousValue();
  }

  public BrailleOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
