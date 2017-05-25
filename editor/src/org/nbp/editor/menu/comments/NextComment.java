package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

import android.view.MenuItem;

public class NextComment extends EditorAction {
  public NextComment () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (!editor.getEditArea().moveToNextComment()) {
      editor.showMessage(R.string.message_no_next_comment);
    }
  }
}
