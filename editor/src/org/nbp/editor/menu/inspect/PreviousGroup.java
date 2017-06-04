package org.nbp.editor.menu.inspect;
import org.nbp.editor.*;

public class PreviousGroup extends EditorAction {
  public PreviousGroup () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!editor.getEditArea().moveToPreviousGroup()) {
      editor.showMessage(R.string.message_no_previous_group);
    }
  }
}
