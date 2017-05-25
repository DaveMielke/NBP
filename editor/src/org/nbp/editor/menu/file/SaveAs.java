package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import android.view.MenuItem;

public class SaveAs extends EditorAction {
  public SaveAs () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    editor.confirmFormat();
  }
}
