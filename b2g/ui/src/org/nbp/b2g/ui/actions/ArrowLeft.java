package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;

public class ArrowLeft extends ArrowAction {
  @Override
  protected boolean performEditAction (Endpoint endpoint) {
    int start = endpoint.getSelectionStart();

    if (endpoint.isSelected(start)) {
      if (start > 0) {
        if (endpoint.setCursor(start-1)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected boolean performSeekAction (Endpoint endpoint) {
    return endpoint.seekPrevious();
  }

  @Override
  protected String getNavigationAction () {
    return "PanLeft";
  }

  @Override
  protected String getScanCode () {
    return "LEFT";
  }

  @Override
  protected int getArrowKeyCode () {
    return KeyEvent.KEYCODE_DPAD_LEFT;
  }

  public ArrowLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
