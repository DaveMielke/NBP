package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleInputOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getBrailleInputControl().nextValue();
  }

  public BrailleInputOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
