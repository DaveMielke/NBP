package org.nbp.editor.menu.selection;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;
import android.view.Menu;

public class Submenu extends EditorAction {
  public Submenu () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    boolean showSelectionActions = false;
    boolean showCursorActions = false;

    if (editor.editArea.hasSelection()) {
      showSelectionActions = true;
    } else {
      showCursorActions = true;
    }

    Menu menu = item.getSubMenu();
    menu.setGroupVisible(R.id.menu_group_selection, showSelectionActions);
    menu.setGroupVisible(R.id.menu_group_cursor, showCursorActions);
  }
}
