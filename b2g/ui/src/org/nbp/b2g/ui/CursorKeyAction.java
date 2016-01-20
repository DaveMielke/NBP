package org.nbp.b2g.ui;

public abstract class CursorKeyAction extends Action {
  protected abstract boolean performCursorKeyAction (Endpoint endpoint, int offset);

  private final boolean allowEnd;

  @Override
  public final boolean performAction (int cursorKey) {
    Endpoint endpoint = getEndpoint();

    if (cursorKey < 0) return false;
    if (cursorKey >= Devices.braille.get().getLength()) return false;

    synchronized (endpoint) {
      int last = endpoint.getBrailleLength();
      if (!(allowEnd && endpoint.isInputArea())) last -= 1;

      int start = endpoint.findFirstBrailleOffset(endpoint.getLineIndent());
      last -= start;
      if (cursorKey > last) return false;

      if (!performCursorKeyAction(endpoint, endpoint.getTextOffset(start + cursorKey))) return false;
    }

    return endpoint.write();
  }

  protected CursorKeyAction (Endpoint endpoint, boolean allowEnd) {
    super(endpoint, false);
    this.allowEnd = allowEnd;
  }
}
