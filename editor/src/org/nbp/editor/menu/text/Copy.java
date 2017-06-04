package org.nbp.editor.menu.text;
import org.nbp.editor.*;

import android.view.MenuItem;

public class Copy extends ClipboardAction {
  public Copy () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    copyToClipboard(editor, false);
  }
}
