package org.nbp.editor;

import android.view.MenuItem;

public abstract class EditorAction {
  protected EditorAction () {
  }

  public void performAction (EditorActivity editor) {
  }

  public void performAction (EditorActivity editor, MenuItem item) {
    performAction(editor);
  }
}
