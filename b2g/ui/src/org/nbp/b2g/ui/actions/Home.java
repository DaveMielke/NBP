package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class Home extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_home();
  }

  public Home (Endpoint endpoint) {
    super(endpoint, false);
  }
}
