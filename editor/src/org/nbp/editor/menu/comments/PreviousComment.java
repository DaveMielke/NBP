package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

public class PreviousComment extends EditorAction {
  public PreviousComment () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToPreviousComment()) {
      editor.showMessage(R.string.message_no_previous_comment);
    }
  }
}
