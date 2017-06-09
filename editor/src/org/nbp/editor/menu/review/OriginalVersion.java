package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class OriginalVersion extends EditorAction {
  public OriginalVersion (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (verifyWritableText()) {
      final EditArea editArea = editor.getEditArea();

      performWithoutRegionProtection(
        new Runnable() {
          @Override
          public void run () {
            Markup.revertRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
