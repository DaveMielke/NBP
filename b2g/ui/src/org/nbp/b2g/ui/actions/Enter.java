package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class Enter extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_enter();
  }

  public Enter (Endpoint endpoint) {
    super(endpoint, false);
  }
}
