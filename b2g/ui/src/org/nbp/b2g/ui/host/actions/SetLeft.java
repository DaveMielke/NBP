package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public class SetLeft extends InputAction {
  @Override
  public boolean performAction (int cursorKey) {
    synchronized (BrailleDevice.LOCK) {
      int offset = cursorKey;

      if (offset < 1) return false;
      if (offset >= BrailleDevice.getBrailleLength()) return false;

      if ((offset += BrailleDevice.getLineIndent()) >= BrailleDevice.getLineLength()) return false;
      BrailleDevice.setLineIndent(offset);
      return BrailleDevice.write();
    }
  }

  public SetLeft () {
    super(false);
  }
}
