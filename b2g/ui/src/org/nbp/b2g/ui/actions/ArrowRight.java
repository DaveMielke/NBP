package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

public class ArrowRight extends ArrowAction {
  @Override
  protected boolean performEditAction (Endpoint endpoint) {
    int end = endpoint.getSelectionEnd();

    if (endpoint.isSelected(end)) {
      if (end < endpoint.getTextLength()) {
        if (end == endpoint.getSelectionStart()) end += 1;
        if (endpoint.setCursor(end)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  protected boolean performSliderAction (Endpoint endpoint) {
    return endpoint.seekNext();
  }

  @Override
  protected String getNavigationAction () {
    return "PanRight";
  }

  public ArrowRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
