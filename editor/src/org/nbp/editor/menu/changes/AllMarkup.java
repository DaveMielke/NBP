package org.nbp.editor.menu.changes;
import org.nbp.editor.*;

import android.view.MenuItem;

public class AllMarkup extends EditorAction {
  public AllMarkup () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    final EditArea editArea = editor.getEditArea();

    editor.runProtectedOperation(
      new Runnable() {
        @Override
        public void run () {
          Markup.restoreRevisions(editArea.getText());
        }
      }
    );
  }
}
