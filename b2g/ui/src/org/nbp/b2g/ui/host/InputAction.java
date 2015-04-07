package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.*;

import android.view.inputmethod.InputConnection;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;

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
    return deleteText(connection, BrailleDevice.getSelectionStart(), BrailleDevice.getSelectionEnd());
  }

  protected ClipboardManager getClipboard () {
    return InputService.getClipboard();
  }

  protected ClipData newTextClip (String text) {
    return ClipData.newPlainText("B2G User Interface", text);
  }

  protected String getClipText (ClipData clip) {
    int count = clip.getItemCount();

    for (int index=0; index<count; index+=1) {
      ClipData.Item item = clip.getItemAt(index);

      if (item != null) {
        CharSequence text = item.getText();
        if (text != null) return text.toString();
      }
    }

    return null;
  }

  protected boolean copyToClipboard (String text) {
    ClipboardManager clipboard = getClipboard();

    if (clipboard != null) {
      clipboard.setPrimaryClip(newTextClip(text));
      return true;
    }

    return false;
  }

  protected InputAction (Endpoint endpoint, boolean isForDevelopers) {
    super(endpoint, isForDevelopers);
  }
}
