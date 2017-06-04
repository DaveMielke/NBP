package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class AcceptChanges extends EditorAction {
  public AcceptChanges () {
    super();
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
