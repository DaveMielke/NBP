package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

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
  protected boolean performSliderAction (Endpoint endpoint) {
    return endpoint.seekPrevious();
  }

  @Override
  protected String getNavigationAction () {
    return "PanLeft";
  }

  public ArrowLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
