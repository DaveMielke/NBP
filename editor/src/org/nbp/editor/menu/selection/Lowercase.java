package org.nbp.editor.menu.selection;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;
import android.text.Editable;

public class Lowercase extends EditorAction {
  public Lowercase () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    EditArea editArea = editor.getEditArea();
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();
    Editable text = editArea.getText();
    text.replace(start, end, text.subSequence(start, end).toString().toLowerCase());
  }
}
