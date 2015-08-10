package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.accessibilityservice.AccessibilityService;

public class MoveRight extends DirectionalAction {
  @Override
  protected boolean performSliderAction (Endpoint endpoint) {
    return endpoint.seekNext();
  }

  @Override
  protected boolean performCursorAction (Endpoint endpoint) {
    int end = endpoint.getSelectionEnd();

    if (endpoint.isSelected(end)) {
      if (end < endpoint.getTextLength()) {
        if (end == endpoint.getSelectionStart()) end += 1;
        if (endpoint.setCursor(end)) {
          return true;
        }
      } else{
        ApplicationUtilities.message(R.string.message_end_of_input_area);
      }
    }

    return false;
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return PanRight.class;
  }

  public MoveRight (Endpoint endpoint) {
    super(endpoint, false);
  }
}
