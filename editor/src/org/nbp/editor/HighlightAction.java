package org.nbp.editor;

import android.view.MenuItem;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.CharacterStyle;

public abstract class HighlightAction extends EditorAction {
  protected HighlightAction () {
    super();
  }

  protected abstract CharacterStyle getCharacterStyle ();

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    EditArea editArea = editor.getEditArea();
    Editable text = editArea.getText();
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (editor.verifyWritableRegion(text, start, end)) {
      if (end > start) {
        text.setSpan(getCharacterStyle(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editArea.setSelection(end);
      }
    }
  }
}
