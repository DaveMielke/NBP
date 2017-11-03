package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ToNextWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return performNextAction(endpoint);
  }

  public ToNextWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
