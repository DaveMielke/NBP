package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;

public class NextRevision extends EditorAction {
  public NextRevision () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextRevision()) {
      editor.showMessage(R.string.message_no_next_revision);
    }
  }
}
