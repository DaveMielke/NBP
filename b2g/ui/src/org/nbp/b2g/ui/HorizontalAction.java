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

  private final CharSequence getCurrentObject (Endpoint endpoint) {
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

    while (from < to) {
      if (!Character.isWhitespace(text.charAt(from))) break;
      from += 1;
    }

    while (to > from) {
      if (!Character.isWhitespace(text.charAt(--to))) {
        to += 1;
        break;
      }
    }

    if (from == to) return null;
    return text.subSequence(from, to);
  }

  protected final ActionResult performSayAction (Endpoint endpoint) {
    CharSequence text = getCurrentObject(endpoint);
    if (text == null) return ActionResult.FAILED;

    ApplicationUtilities.say(text);
    return ActionResult.DONE;
  }

  protected final ActionResult performSpellAction (Endpoint endpoint) {
    CharSequence text = getCurrentObject(endpoint);
    if (text == null) return ActionResult.FAILED;

    int count = text.length();
    CharSequence[] characters = new CharSequence[count];

    for (int index=0; index<count; index+=1) {
      characters[index] = Character.toString(text.charAt(index));
    }

    ApplicationUtilities.say(characters);
    return ActionResult.DONE;
  }

  protected HorizontalAction (Endpoint endpoint, boolean isAdvanced) {
    super(endpoint, isAdvanced);
  }
}
