package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleInputOff extends Action {
  @Override
  public boolean performAction () {
    return Controls.getBrailleInputControl().previousValue();
  }

  public BrailleInputOff (Endpoint endpoint) {
    super(endpoint, false);
  }
}
