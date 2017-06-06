package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;

public class PreviousRevision extends EditorAction {
  public PreviousRevision (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToPreviousRevision()) {
      editor.showMessage(R.string.message_no_previous_revision);
    }
  }
}
