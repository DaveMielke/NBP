package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DropRight extends DragAction {
  @Override
  public boolean performAction () {
    if (haveFromRegion()) {
      if (dropRight(getRegion())) {
        return true;
      }
    }

    return false;
  }

  public DropRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
