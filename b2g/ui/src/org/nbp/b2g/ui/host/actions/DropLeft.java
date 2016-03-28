package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DropLeft extends DragAction {
  @Override
  public boolean performAction () {
    if (haveFromRegion()) {
      if (dropLeft(getRegion())) {
        return true;
      }
    }

    return false;
  }

  public DropLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
