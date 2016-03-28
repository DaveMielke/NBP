package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DragLeft extends DragAction {
  @Override
  public boolean performAction () {
    if (setFromRegion()) {
      if (dropLeft(getFromRegion())) {
        return true;
      }
    }

    return false;
  }

  public DragLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
