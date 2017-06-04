package org.nbp.editor.menu.text;
import org.nbp.editor.*;

import android.view.MenuItem;

public class SelectAll extends EditorAction {
  public SelectAll () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    editor.getEditArea().selectAll();
  }
}
