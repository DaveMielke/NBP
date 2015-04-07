package org.nbp.b2g.ui.host.actions;
import org.nbp.b2g.ui.host.*;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowDown extends ArrowAction {
  @Override
  protected boolean performArrowEditAction () {
    int end = BrailleDevice.getSelectionEnd();

    if (BrailleDevice.isSelected(end)) {
      if (end != BrailleDevice.getSelectionStart()) end -= 1;
      int start = BrailleDevice.findNextNewline(end);

      if (start != -1) {
        start += 1;
        int length = BrailleDevice.findNextNewline(start);
        if (length == -1) length = BrailleDevice.getTextLength();
        length -= start;

        int before = BrailleDevice.findPreviousNewline(end);
        if (before != -1) end -= before + 1;
        if (end > length) end = length;
        end += start;

        if (setCursor(end)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected String getScanCode () {
    return "DOWN";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_DOWN;
  }

  public ArrowDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
