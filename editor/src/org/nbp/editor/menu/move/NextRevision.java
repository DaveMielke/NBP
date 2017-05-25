package org.nbp.editor.menu.move;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;

public class NextRevision extends EditorAction {
  public NextRevision () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (!editor.getEditArea().moveToNextRevision()) {
      editor.showMessage(R.string.message_no_next_revision);
    }
  }
}
