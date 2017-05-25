package org.nbp.editor.menu.comments;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;

public class PreviousComment extends EditorAction {
  public PreviousComment () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (!editor.getEditArea().moveToPreviousComment()) {
      editor.showMessage(R.string.message_no_previous_comment);
    }
  }
}
