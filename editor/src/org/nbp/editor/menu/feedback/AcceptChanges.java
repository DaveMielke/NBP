package org.nbp.editor.menu.feedback;
import org.nbp.editor.*;

public class AcceptChanges extends EditorAction {
  public AcceptChanges (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (editor.verifyWritableText()) {
      final EditArea editArea = editor.getEditArea();

      editor.performWithoutRegionProtection(
        new Runnable() {
          @Override
          public void run () {
            if (Markup.acceptRevisions(editArea.getText())) {
              editArea.setHasChanged();
            }
          }
        }
      );
    }
  }
}
