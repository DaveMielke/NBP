package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class Save extends SaveAs {
  public Save () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (editor.getEditArea().getContentHandle() == null) {
      super.performAction(editor);
    } else {
      editor.saveFile();
    }
  }
}
