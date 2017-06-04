package org.nbp.editor.menu.proofread;
import org.nbp.editor.*;

public class DraftVersion extends EditorAction {
  public DraftVersion () {
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
            Markup.applyRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
