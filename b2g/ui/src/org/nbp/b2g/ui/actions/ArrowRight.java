package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowRight extends ArrowAction {
  @Override
  protected boolean performArrowEditAction () {
    int end = BrailleDevice.getSelectionEnd();

    if (BrailleDevice.isSelected(end)) {
      if (end < BrailleDevice.getTextLength()) {
        if (end == BrailleDevice.getSelectionStart()) end += 1;
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

  public ArrowRight () {
    super(false);
  }
}
