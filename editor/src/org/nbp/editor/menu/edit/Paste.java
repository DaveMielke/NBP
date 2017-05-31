package org.nbp.editor.menu.edit;
import org.nbp.editor.*;

import android.view.MenuItem;

public class Paste extends ClipboardAction {
  public Paste () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (editor.verifyWritableRegion()) {
      CharSequence text = getText(getClipboard(editor));
      if (text != null) editor.getEditArea().replaceSelection(text);
    }
  }
}
