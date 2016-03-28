package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DragFrom extends DragAction {
  @Override
  public boolean performAction () {
    return setFromRegion();
  }

  public DragFrom (Endpoint endpoint) {
    super(endpoint, false);
  }
}
