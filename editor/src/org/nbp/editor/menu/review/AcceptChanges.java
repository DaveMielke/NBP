package org.nbp.editor.menu.review;
import org.nbp.editor.*;

import android.view.MenuItem;

public class AcceptChanges extends EditorAction {
  public AcceptChanges () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    final EditArea editArea = editor.getEditArea();

    editor.runProtectedOperation(
      new Runnable() {
        @Override
        public void run () {
          if (Markup.acceptRevisions(editArea.getText())) {
            editArea.setHasChanged();
          }
        }
      }
    );
  }
}
