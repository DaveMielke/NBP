package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class SelectAll extends EditorAction {
  public SelectAll (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    editor.getEditArea().selectAll();
  }
}
