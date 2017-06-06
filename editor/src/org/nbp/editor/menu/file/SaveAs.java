package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class SaveAs extends EditorAction {
  public SaveAs (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    editor.confirmFormat();
  }
}
