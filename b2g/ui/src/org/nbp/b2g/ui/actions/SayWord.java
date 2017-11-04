package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SayWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return performSayAction(endpoint);
  }

  public SayWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
