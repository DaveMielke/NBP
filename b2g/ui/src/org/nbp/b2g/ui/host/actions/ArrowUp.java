package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowUp extends ArrowAction {
  @Override
  protected boolean performArrowEditAction (Endpoint endpoint) {
    int start = endpoint.getSelectionStart();

    if (endpoint.isSelected(start)) {
      int after = endpoint.findPreviousNewline(start);

      if (after != -1) {
        int offset = start - after - 1;

        int before = endpoint.findPreviousNewline(after);
        start = (before == -1)? 0: (before + 1);

        int length = after - start;
        if (offset > length) offset = length;
        start += offset;

        if (setCursor(start)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected String getScanCode () {
    return "UP";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_UP;
  }

  public ArrowUp (Endpoint endpoint) {
    super(endpoint, false);
  }
}
