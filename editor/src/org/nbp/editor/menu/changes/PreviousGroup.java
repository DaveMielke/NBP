package org.nbp.editor.menu.changes;
import org.nbp.editor.*;

import android.view.MenuItem;

public class PreviousGroup extends EditorAction {
  public PreviousGroup () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (!editor.getEditArea().moveToPreviousGroup()) {
      editor.showMessage(R.string.message_no_previous_group);
    }
  }
}
