package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class ScrollDown extends DirectionalAction {
  @Override
  protected boolean performInternalAction (Endpoint endpoint) {
    int textLength = endpoint.getTextLength();
    int brailleLength = getBrailleLength();

    if ((endpoint.getLineStart() + endpoint.getLineLength()) == textLength) {
      if ((endpoint.getBrailleStart() + brailleLength) >= textLength) {
        return false;
      }
    }

    int indent = endpoint.setLine(textLength) - brailleLength;
    if (indent < 0) indent = 0;

    endpoint.setLine(endpoint.getLineStart() + indent);
    endpoint.setLineIndent(indent);

    return endpoint.write();
  }

  @Override
  protected Class<? extends Action> getExternalAction () {
    return getEndpoint().getScrollForwardAction();
  }

  public ScrollDown (Endpoint endpoint) {
    super(endpoint, false);
  }
}
