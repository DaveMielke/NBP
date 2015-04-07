package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowLeft extends ArrowAction {
  @Override
  protected boolean performArrowEditAction () {
    int start = BrailleDevice.getSelectionStart();

    if (BrailleDevice.isSelected(start)) {
      if (start > 0) {
        if (setCursor(start-1)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected String getScanCode () {
    return "LEFT";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_LEFT;
  }

  public ArrowLeft () {
    super(false);
  }
}
