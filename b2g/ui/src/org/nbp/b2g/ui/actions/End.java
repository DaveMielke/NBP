package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class End extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_end();
  }

  public End (Endpoint endpoint) {
    super(endpoint, false);
  }
}
