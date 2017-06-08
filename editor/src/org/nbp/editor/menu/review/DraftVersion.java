package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class DraftVersion extends EditorAction {
  public DraftVersion (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (editor.verifyWritableText()) {
      final EditArea editArea = editor.getEditArea();

      performWithoutRegionProtection(
        new Runnable() {
          @Override
          public void run () {
            Markup.applyRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
