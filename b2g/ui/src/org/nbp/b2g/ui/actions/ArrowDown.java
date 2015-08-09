package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ArrowDown extends ArrowAction {
  @Override
  protected boolean performInputAction (Endpoint endpoint) {
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
      } else{
        ApplicationUtilities.message(R.string.message_bottom_of_input_area);
      }
    }

    return false;
  }

  @Override
  protected Class<? extends Action> getMoveAction () {
    return LineNext.class;
  }

  @Override
  protected Class<? extends Action> getNavigationAction () {
    return getEndpoint().getMoveForwardAction();
  }

  public ArrowDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
