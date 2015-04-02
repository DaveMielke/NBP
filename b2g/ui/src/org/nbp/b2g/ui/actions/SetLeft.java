package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetLeft extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      return BrailleDevice.scrollRight(cursorKey);
    }
  }

  public SetLeft () {
    super();
  }
}
