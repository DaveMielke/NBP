package org.nbp.editor.menu.inspect;
import org.nbp.editor.*;

public class NextGroup extends EditorAction {
  public NextGroup () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToNextGroup()) {
      editor.showMessage(R.string.message_no_next_group);
    }
  }
}
