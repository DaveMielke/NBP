package org.nbp.editor;

import android.view.MenuItem;
import android.view.Menu;

public abstract class MenuAction extends EditorAction {
  protected MenuAction () {
    super();
  }

  protected void prepareMenu (EditorActivity editor, Menu menu) {
    editor.currentMenu = menu;
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    prepareMenu(editor, item.getSubMenu());
  }
}
