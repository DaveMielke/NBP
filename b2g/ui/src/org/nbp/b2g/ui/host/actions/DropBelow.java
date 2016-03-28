package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DropBelow extends DragAction {
  @Override
  public boolean performAction () {
    if (haveFromRegion()) {
      if (dropBelow(getRegion())) {
        return true;
      }
    }

    return false;
  }

  public DropBelow (Endpoint endpoint) {
    super(endpoint, false);
  }
}
