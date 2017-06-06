package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

public class NextComment extends EditorAction {
  public NextComment (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextComment()) {
      editor.showMessage(R.string.message_no_next_comment);
    }
  }
}
