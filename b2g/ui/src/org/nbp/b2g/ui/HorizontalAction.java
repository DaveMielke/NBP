package org.nbp.b2g.ui;

public abstract class HorizontalAction extends DirectionalAction {
  @Override
  public ActionResult performInternalAction (Endpoint endpoint) {
    return performCursorAction(endpoint);
  }

  private final int getInternalPosition (Endpoint endpoint) {
    return endpoint.getLineStart() + endpoint.getLineIndent();
  }

  protected final int getSelectionStart (Endpoint endpoint) {
    return endpoint.isInputArea()?
           endpoint.getSelectionStart():
           getInternalPosition(endpoint);
  }

  protected final int getSelectionEnd (Endpoint endpoint) {
    return endpoint.isInputArea()?
           endpoint.getSelectionEnd():
           getInternalPosition(endpoint);
  }

  protected final ActionResult setCursor (Endpoint endpoint, int offset) {
    endpoint.setLine(offset);

    if (endpoint.isInputArea()) {
      endpoint.setCursor(offset);
      return ActionResult.DONE;
    } else {
      endpoint.setLineIndent(offset - endpoint.getLineStart());
      return ActionResult.WRITE;
    }
  }

  protected final static int NOT_FOUND = -1;
  protected abstract int findNextObject (Endpoint endpoint, int offset);
  protected abstract int findPreviousObject (Endpoint endpoint, int offset);

  protected final ActionResult performNextAction (Endpoint endpoint) {
    int current = getSelectionEnd(endpoint);
    if (current != getSelectionStart(endpoint)) current -= 1;

    int next = findNextObject(endpoint, current);
    if (next == NOT_FOUND) return ActionResult.FAILED;
    return setCursor(endpoint, next);
  }

  protected final ActionResult performPreviousAction (Endpoint endpoint) {
    int current = getSelectionStart(endpoint);
    int previous = findPreviousObject(endpoint, current);
    if (previous == NOT_FOUND) return ActionResult.FAILED;
    return setCursor(endpoint, previous);
  }

  protected HorizontalAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
