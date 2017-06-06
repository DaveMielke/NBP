package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class NextChange extends EditorAction {
  public NextChange (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextChange()) {
      editor.showMessage(R.string.message_no_next_change);
    }
  }
}
