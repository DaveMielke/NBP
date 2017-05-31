package org.nbp.editor.menu.changes;
import org.nbp.editor.*;

import android.view.MenuItem;

public class Original extends EditorAction {
  public Original () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (editor.verifyWritableText()) {
      final EditArea editArea = editor.getEditArea();

      editor.runProtectedOperation(
        new Runnable() {
          @Override
          public void run () {
            Markup.revertRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
