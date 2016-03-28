package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DragRight extends DragAction {
  @Override
  public boolean performAction () {
    if (setFromRegion()) {
      if (dropRight(getFromRegion())) {
        return true;
      }
    }

    return false;
  }

  public DragRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
