package org.nbp.editor;

import android.content.ClipboardManager;
import android.content.ClipData;

import android.content.Context;
import android.text.Editable;

public abstract class ClipboardAction extends EditorAction {
  protected ClipboardAction (EditorActivity editor) {
    super(editor);
  }

  protected final ClipboardManager getClipboard () {
    return (ClipboardManager)getEditor().getSystemService(Context.CLIPBOARD_SERVICE);
  }

  protected final CharSequence getText (ClipData clip) {
    int count = clip.getItemCount();

    for (int index=0; index<count; index+=1) {
      ClipData.Item item = clip.getItemAt(index);
      if (item == null) continue;

      CharSequence text = item.getText();
      if (text != null) return text;
    }

    return null;
  }

  protected final CharSequence getText (ClipboardManager clipboard) {
    synchronized (clipboard) {
      ClipData clip = clipboard.getPrimaryClip();
      if (clip != null) return getText(clip);
    }

    return null;
  }

  protected final void copyToClipboard (boolean delete) {
    EditArea editArea = getEditArea();
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    Editable text = editArea.getText();
    ClipData clip = ClipData.newPlainText("NBP Editor", text.subSequence(start, end));
    getClipboard().setPrimaryClip(clip);

    if (delete) {
      text.delete(start, end);
    } else {
      editArea.setSelection(end);
    }
  }
}
