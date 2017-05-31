package org.nbp.editor.menu.changes;
import org.nbp.editor.*;

import android.view.MenuItem;

public class NoMarkup extends EditorAction {
  public NoMarkup () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (editor.verifyWritableText()) {
      final EditArea editArea = editor.getEditArea();

      editor.performWithoutRegionProtection(
        new Runnable() {
          @Override
          public void run () {
            Markup.applyRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
