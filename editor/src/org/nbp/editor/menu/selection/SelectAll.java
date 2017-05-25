package org.nbp.editor.menu.selection;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

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
