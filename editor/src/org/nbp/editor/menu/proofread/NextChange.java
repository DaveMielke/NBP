package org.nbp.editor.menu.proofread;
import org.nbp.editor.*;

public class NextChange extends EditorAction {
  public NextChange () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextGroup()) {
      editor.showMessage(R.string.message_no_next_change);
    }
  }
}
