package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ToPreviousWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return performPreviousAction(endpoint);
  }

  public ToPreviousWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
