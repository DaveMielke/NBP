package org.nbp.b2g.ui;

import android.view.inputmethod.InputConnection;

public abstract class InputAction extends ScanCodeAction {
  protected int getSelectionOffset (int cursorKey) {
    return BrailleDevice.getLineStart() + BrailleDevice.getLineIndent() + cursorKey;
  }

  protected boolean isCharacterOffset (int offset) {
    offset -= BrailleDevice.getLineStart();
    return ((offset >= 0) && (offset < BrailleDevice.getLineLength()));
  }

  protected boolean isCursorOffset (int offset) {
    offset -= BrailleDevice.getLineStart();
    return ((offset >= 0) && (offset <= BrailleDevice.getLineLength()));
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
