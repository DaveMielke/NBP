package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

public abstract class InputAction extends HostAction {
  protected int getSelectionOffset (int cursorKey) {
    return getEndpoint().getBrailleStart() + cursorKey;
  }

  private int toLineOffset (int selectionOffset) {
    return selectionOffset - getEndpoint().getLineStart();
  }

  protected boolean isCharacterOffset (int selectionOffset) {
    int lineOffset = toLineOffset(selectionOffset);
    return ((lineOffset >= 0) && (lineOffset < getEndpoint().getLineLength()));
  }

  protected boolean isCursorOffset (int selectionOffset) {
    int lineOffset = toLineOffset(selectionOffset);
    return ((lineOffset >= 0) && (lineOffset <= getEndpoint().getLineLength()));
  }

  protected boolean setCursor (InputConnection connection, int offset) {
    return connection.setSelection(offset, offset);
  }

  protected boolean setCursor (int offset) {
    InputConnection connection = getInputConnection();

    if (connection != null) {
      if (setCursor(connection, offset)) {
        return true;
      }
    }

    return false;
  }

  protected boolean deleteText (InputConnection connection, int start, int end) {
    if (connection.beginBatchEdit()) {
      if (setCursor(connection, end)) {
        if (connection.deleteSurroundingText((end - start), 0)) {
          if (connection.endBatchEdit()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  protected boolean deleteSelectedText (InputConnection connection) {
    Endpoint endpoint = getEndpoint();
    return deleteText(connection, endpoint.getSelectionStart(), endpoint.getSelectionEnd());
  }

  protected InputAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
