package org.nbp.editor.menu.proofread;
import org.nbp.editor.*;

public class MarkupVersion extends EditorAction {
  public MarkupVersion () {
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
            Markup.restoreRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
