package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowDown extends ArrowAction {
  @Override
  protected boolean performEditAction (Endpoint endpoint) {
    int end = endpoint.getSelectionEnd();

    if (endpoint.isSelected(end)) {
      if (end != endpoint.getSelectionStart()) end -= 1;
      int start = endpoint.findNextNewline(end);

      if (start != -1) {
        start += 1;
        int length = endpoint.findNextNewline(start);
        if (length == -1) length = endpoint.getTextLength();
        length -= start;

        int before = endpoint.findPreviousNewline(end);
        if (before != -1) end -= before + 1;
        if (end > length) end = length;
        end += start;

        if (endpoint.setCursor(end)) {
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
