package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

public class DragFrom extends DragAction {
  @Override
  public boolean performAction () {
    if (setFromRegion()) {
      ApplicationUtilities.message(R.string.drag_message_begin);
      return true;
    }
    
    return false;
  }

  public DragFrom (Endpoint endpoint) {
    super(endpoint, false);
  }
}
