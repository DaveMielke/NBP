package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PageUp extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_pageUp();
  }

  public PageUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
