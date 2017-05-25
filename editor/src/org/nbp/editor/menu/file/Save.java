package org.nbp.editor.menu.file;
import org.nbp.editor.menu.*;;
import org.nbp.editor.*;;

import android.view.MenuItem;

public class Save extends SaveAs {
  public Save () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor, MenuItem item) {
    if (editor.getContentHandle() == null) {
      super.performAction(editor, item);
    } else {
      editor.saveFile();
    }
  }
}
