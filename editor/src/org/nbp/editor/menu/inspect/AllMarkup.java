package org.nbp.editor.menu.inspect;
import org.nbp.editor.*;

import android.view.MenuItem;

public class AllMarkup extends EditorAction {
  public AllMarkup () {
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
            Markup.restoreRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
