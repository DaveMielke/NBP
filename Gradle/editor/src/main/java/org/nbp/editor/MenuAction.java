package org.nbp.editor;

import android.view.MenuItem;
import android.view.Menu;

public abstract class MenuAction extends EditorAction {
  protected MenuAction (EditorActivity editor) {
    super(editor);
  }

  private static Menu currentMenu = null;

  public final static Menu getCurrentMenu () {
    return currentMenu;
  }

  public final static void setCurrentMenu (Menu menu) {
    currentMenu = menu;
  }

  protected void prepareMenu (Menu menu) {
    setCurrentMenu(menu);
  }

  @Override
  public void performAction (MenuItem item) {
    prepareMenu(item.getSubMenu());
  }
}
