package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DragDown extends DragAction {
  @Override
  public boolean performAction () {
    if (setFromRegion()) {
      if (dropBelow(getFromRegion())) {
        return true;
      }
    }

    return false;
  }

  public DragDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
