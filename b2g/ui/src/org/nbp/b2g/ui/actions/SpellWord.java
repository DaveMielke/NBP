package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SpellWord extends WordAction {
  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    return performSpellAction(endpoint);
  }

  public SpellWord (Endpoint endpoint) {
    super(endpoint, false);
  }
}
