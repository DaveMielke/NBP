package org.nbp.editor;

import android.view.MenuItem;
import android.text.Editable;

public abstract class CharacterAction extends EditorAction {
  protected CharacterAction () {
    super();
  }

  protected abstract boolean testCharacter (char character);
  protected abstract char translateCharacter (char character);

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    EditArea editArea = editor.getEditArea();
    Editable text = editArea.getText();
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    StringBuilder replacement = new StringBuilder();
    replacement.append(' ');

    while (start < end) {
      char character = text.charAt(start);
      int next = start + 1;

      if (testCharacter(character)) {
        replacement.setCharAt(0, translateCharacter(character));
        text.replace(start, next, replacement);
      }

      start = next;
    }

    editor.getEditArea().setSelection(end);
  }
}
