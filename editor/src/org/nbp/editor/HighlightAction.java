package org.nbp.editor;

import android.view.MenuItem;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.CharacterStyle;

public abstract class HighlightAction extends EditorAction {
  protected HighlightAction () {
    super();
  }

  protected final void addStyle (EditorActivity editor, CharacterStyle style) {
    EditArea editArea = editor.getEditArea();
    Editable text = editArea.getText();

    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (end > start) {
      text.setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      editArea.setSelection(end);
    }
  }
}
