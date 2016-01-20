package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class SetLeft extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int indent) {
    endpoint.setLineIndent(indent);
    return true;
  }

  public SetLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
