package org.nbp.editor.menu.text;
import org.nbp.editor.*;

import android.view.Menu;

public class Submenu extends MenuAction {
  public Submenu (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void prepareMenu (Menu menu) {
    super.prepareMenu(menu);

    boolean showSelectionActions = false;
    boolean showCursorActions = false;

    if (getEditArea().hasSelection()) {
      showSelectionActions = true;
    } else {
      showCursorActions = true;
    }

    menu.setGroupVisible(R.id.menu_group_selection, showSelectionActions);
    menu.setGroupVisible(R.id.menu_group_cursor, showCursorActions);
  }
}
