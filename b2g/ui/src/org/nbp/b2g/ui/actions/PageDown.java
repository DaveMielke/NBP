package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class PageDown extends Action {
  @Override
  public boolean performAction () {
    return getEndpoint().handleKeyboardKey_pageDown();
  }

  public PageDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
