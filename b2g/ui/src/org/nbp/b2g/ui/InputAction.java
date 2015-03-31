package org.nbp.b2g.ui;

import android.view.inputmethod.InputConnection;

public abstract class InputAction extends ScanCodeAction {
  protected int getOffset (int cursorKey) {
    return BrailleDevice.getIndent() + cursorKey;
  }

  protected boolean isCharacterOffset (int offset) {
    return ((offset >= 0) && (offset < BrailleDevice.getLength()));
  }

  protected boolean isCursorOffset (int offset) {
    return ((offset >= 0) && (offset <= BrailleDevice.getLength()));
  }

  protected boolean isSelected (int offset) {
    return offset != InputService.NO_SELECTION;
  }

  protected boolean isSelected (int start, int end) {
    return (start != end) && isSelected(start) && isSelected(end);
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

  protected InputAction () {
    super();
  }
}
