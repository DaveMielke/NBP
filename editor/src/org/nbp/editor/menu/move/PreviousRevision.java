package org.nbp.editor.menu.move;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;

public class PreviousRevision extends EditorAction {
  public PreviousRevision () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (!editor.getEditArea().moveToPreviousRevision()) {
      editor.showMessage(R.string.message_no_previous_revision);
    }
  }
}
