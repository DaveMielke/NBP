package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class OriginalVersion extends EditorAction {
  public OriginalVersion () {
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
            Markup.revertRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
