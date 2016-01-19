package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

public class MoveLeft extends DirectionalAction {
  @Override
  protected ActionResult performSliderAction (Endpoint endpoint) {
    return endpoint.seekPrevious()? ActionResult.DONE: ActionResult.FAILED;
  }

  @Override
  protected ActionResult performCursorAction (Endpoint endpoint) {
    int start = endpoint.getSelectionStart();

    if (Endpoint.isSelected(start)) {
      if (start > 0) {
        if (endpoint.setCursor(start-1)) {
          return ActionResult.DONE;
        }
      } else {
        ApplicationUtilities.message(R.string.message_start_of_input_area);
      }
    }

    return ActionResult.FAILED;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return PanLeft.class;
  }

  public MoveLeft (Endpoint endpoint) {
    super(endpoint, false);
  }
}
