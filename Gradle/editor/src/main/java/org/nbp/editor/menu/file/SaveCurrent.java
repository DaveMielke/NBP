package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class SaveCurrent extends SaveAs {
  public SaveCurrent (EditorActivity editor) {
    super(editor);
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
