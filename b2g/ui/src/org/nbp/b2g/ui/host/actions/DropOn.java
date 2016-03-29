package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DropOn extends DragAction {
  @Override
  public boolean performAction () {
    if (haveFromRegion()) {
      if (dropAt(getRegion())) {
        return true;
      }
    }

    return false;
  }

  public DropOn (Endpoint endpoint) {
    super(endpoint, false);
  }
}
