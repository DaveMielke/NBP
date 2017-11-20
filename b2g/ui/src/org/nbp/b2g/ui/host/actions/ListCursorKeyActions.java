package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class ListCursorKeyActions extends Action {
  @Override
  public boolean performAction (int cursorKey) {
    ActionChooser.chooseAction(getEndpoint().getRootKeyBindingMap(), cursorKey);
    return true;
  }

  public ListCursorKeyActions (Endpoint endpoint) {
    super(endpoint, false);
  }
}
