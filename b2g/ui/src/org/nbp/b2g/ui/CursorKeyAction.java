package org.nbp.b2g.ui;

public abstract class CursorKeyAction extends Action {
  protected abstract boolean performCursorKeyAction (Endpoint endpoint, int offset);

  private final boolean allowEnd;

  @Override
  public final boolean performAction (int cursorKey) {
    if (cursorKey < 0) return false;
    int brailleLength = Devices.braille.get().getLength();
    if (cursorKey >= brailleLength) return false;
    Endpoint endpoint = getEndpoint();

    synchronized (endpoint) {
      int lineLength = endpoint.getLineLength();
      int lineIndent = endpoint.getLineIndent();
      int nextSegment = endpoint.findNextSegment(brailleLength, lineIndent);
      int lastCell;

      if (nextSegment < lineLength) {
        lastCell = endpoint.findFirstBrailleOffset(nextSegment) - 1;
      } else if (allowEnd && endpoint.isInputArea()) {
        lastCell = endpoint.getBrailleLength();
      } else {
        lastCell = endpoint.findFirstBrailleOffset(lineLength) - 1;
      }

      int brailleIndent = endpoint.findFirstBrailleOffset(lineIndent);
      lastCell -= brailleIndent;
      if (cursorKey > lastCell) cursorKey = lastCell;

      if (!performCursorKeyAction(endpoint, endpoint.getLineOffset(brailleIndent + cursorKey))) return false;
      return endpoint.write();
    }
  }

  protected CursorKeyAction (Endpoint endpoint, boolean allowEnd) {
    super(endpoint, false);
    this.allowEnd = allowEnd;
  }
}
