package org.nbp.b2g.ui;

import android.view.inputmethod.InputConnection;

public abstract class InputAction extends ScanCodeAction {
  protected int getSelectionOffset (int cursorKey) {
    return BrailleDevice.getBrailleStart() + cursorKey;
  }

  private int toLineOffset (int selectionOffset) {
    return selectionOffset - BrailleDevice.getLineStart();
  }

  protected boolean isCharacterOffset (int selectionOffset) {
    int lineOffset = toLineOffset(selectionOffset);
    return ((lineOffset >= 0) && (lineOffset < BrailleDevice.getLineLength()));
  }

  protected boolean isCursorOffset (int selectionOffset) {
    int lineOffset = toLineOffset(selectionOffset);
    return ((lineOffset >= 0) && (lineOffset <= BrailleDevice.getLineLength()));
  }

  protected boolean deleteText (InputConnection connection, int start, int end) {
    if (connection.beginBatchEdit()) {
      if (connection.setSelection(end, end)) {
        if (connection.deleteSurroundingText((end - start), 0)) {
          if (connection.endBatchEdit()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  protected boolean deleteText (InputConnection connection) {
    return deleteText(connection, BrailleDevice.getSelectionStart(), BrailleDevice.getSelectionEnd());
  }

  protected InputAction () {
    super();
  }
}
