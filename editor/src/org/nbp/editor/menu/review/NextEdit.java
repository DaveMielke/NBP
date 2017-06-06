package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class NextEdit extends EditorAction {
  public NextEdit (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextEdit()) {
      editor.showMessage(R.string.message_no_next_edit);
    }
  }
}
