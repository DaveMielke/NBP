package org.nbp.editor.menu.text;
import org.nbp.editor.*;

import android.text.Editable;

public abstract class TextAction extends EditorAction {
  protected TextAction (EditorActivity editor) {
    super(editor);
  }

  protected abstract boolean testCharacter (char character);
  protected abstract char translateCharacter (char character);

  @Override
  public void performAction (EditorActivity editor) {
    EditArea editArea = editor.getEditArea();
    Editable text = editArea.getText();

    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (verifyWritableRegion(text, start, end)) {
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
}
