package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetLeft extends ScreenAction {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      return BrailleDevice.shiftRight(cursorKey);
    }
  }

  public SetLeft () {
    super();
  }
}
