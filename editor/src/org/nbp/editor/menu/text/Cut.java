package org.nbp.editor.menu.text;
import org.nbp.editor.*;

import android.view.MenuItem;

public class Cut extends ClipboardAction {
  public Cut () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    copyToClipboard(editor, true);
  }
}
