package org.nbp.editor.menu.proofread;
import org.nbp.editor.*;

public class PreviousChange extends EditorAction {
  public PreviousChange () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToPreviousGroup()) {
      editor.showMessage(R.string.message_no_previous_change);
    }
  }
}
