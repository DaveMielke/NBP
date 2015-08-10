package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

public class MoveLeft extends DirectionalAction {
  @Override
  protected boolean performSliderAction (Endpoint endpoint) {
    return endpoint.seekPrevious();
  }

  @Override
  protected boolean performCursorAction (Endpoint endpoint) {
    int start = endpoint.getSelectionStart();

    if (endpoint.isSelected(start)) {
      if (start > 0) {
        if (endpoint.setCursor(start-1)) {
          return true;
        }
      } else {
        ApplicationUtilities.message(R.string.message_start_of_input_area);
      }
    }

    return false;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return PanLeft.class;
  }

  public MoveLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
