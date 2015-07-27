package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class BrailleOn extends Action {
  @Override
  public boolean performAction () {
    return Controls.getBrailleEnabledControl().nextValue();
  }

  public BrailleOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
