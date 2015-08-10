package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollDown extends DirectionalAction {
  private boolean scrollText (Endpoint endpoint, boolean isInputArea) {
    int brailleLength = getBrailleLength();
    int textLength = endpoint.getTextLength();

    int textEnd = textLength;
    if (isInputArea) textEnd += 1;

    if ((endpoint.getLineStart() + endpoint.getLineLength()) == textLength) {
      if ((endpoint.getBrailleStart() + brailleLength) >= textEnd) {
        return false;
      }
    }

    int indent = endpoint.setLine(textEnd) - brailleLength;
    if (indent < 0) indent = 0;

    endpoint.setLine(endpoint.getLineStart() + indent);
    endpoint.setLineIndent(indent);

    return endpoint.write();
  }

  @Override
  protected boolean performCursorAction (Endpoint endpoint) {
    return scrollText(endpoint, true);
  }

  @Override
  protected boolean performInternalAction (Endpoint endpoint) {
    return scrollText(endpoint, false);
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollForwardAction();
  }

  public ScrollDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
