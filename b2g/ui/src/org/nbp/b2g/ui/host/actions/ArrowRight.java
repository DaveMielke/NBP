package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowRight extends ArrowAction {
  @Override
  protected boolean performArrowEditAction (Endpoint endpoint) {
    int end = endpoint.getSelectionEnd();

    if (endpoint.isSelected(end)) {
      if (end < endpoint.getTextLength()) {
        if (end == endpoint.getSelectionStart()) end += 1;
        if (setCursor(end)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected String getScanCode () {
    return "RIGHT";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_RIGHT;
  }

  public ArrowRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
