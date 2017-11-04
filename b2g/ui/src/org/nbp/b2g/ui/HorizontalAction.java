package org.nbp.b2g.ui;

public abstract class HorizontalAction extends DirectionalAction {
  @Override
  public ActionResult performInternalAction (Endpoint endpoint) {
    return performCursorAction(endpoint);
  }

  private final int getInternalPosition (Endpoint endpoint) {
    return endpoint.getLineStart() + endpoint.getLineIndent();
  }

  protected final int getFirstPosition (Endpoint endpoint) {
    if (!endpoint.isInputArea()) return getInternalPosition(endpoint);
    return endpoint.getSelectionStart();
  }

  protected final int getLastPosition (Endpoint endpoint) {
    if (!endpoint.isInputArea()) return getInternalPosition(endpoint);
    int position = endpoint.getSelectionEnd();
    if (position != getFirstPosition(endpoint)) position -= 1;
    return position;
  }

  protected final ActionResult setCursor (Endpoint endpoint, int offset) {
    endpoint.setLine(offset);

    {
      CharSequence text = endpoint.getText();
      int end = findNextObject(endpoint, offset);
      if (end == NOT_FOUND) end = text.length();
      ApplicationUtilities.say(text.subSequence(offset, end));
    }

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
    int current = getLastPosition(endpoint);
    int next = findNextObject(endpoint, current);

    if (next == NOT_FOUND) return ActionResult.FAILED;
    return setCursor(endpoint, next);
  }

  protected final ActionResult performPreviousAction (Endpoint endpoint) {
    int current = getFirstPosition(endpoint);
    int previous = findPreviousObject(endpoint, current);

    if (previous == NOT_FOUND) return ActionResult.FAILED;
    return setCursor(endpoint, previous);
  }

  protected final ActionResult performSayAction (Endpoint endpoint) {
    CharSequence text = endpoint.getText();
    int length = text.length();

    int from = getFirstPosition(endpoint);
    int to = getLastPosition(endpoint);

    {
      int offset = from;
      if (offset < length) offset += 1;

      int previous = findPreviousObject(endpoint, offset);
      if (previous != NOT_FOUND) from = previous;
    }

    {
      int next = findNextObject(endpoint, to);
      if (next == NOT_FOUND) next = length;
      to = next;
    }

    ApplicationUtilities.say(text.subSequence(from, to));
    return ActionResult.FAILED;
  }

  protected HorizontalAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
