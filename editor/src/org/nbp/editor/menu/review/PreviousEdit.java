package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class PreviousEdit extends EditorAction {
  public PreviousEdit (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToPreviousEdit()) {
      editor.showMessage(R.string.message_no_previous_edit);
    }
  }
}
