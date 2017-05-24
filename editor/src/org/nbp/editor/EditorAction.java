package org.nbp.editor;

import android.view.MenuItem;

public abstract class EditorAction {
  protected EditorAction () {
  }

  public abstract void performAction (EditorActivity editor, MenuItem item);
}
