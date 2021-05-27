package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class MarkupVersion extends EditorAction {
  public MarkupVersion (EditorActivity editor) {
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
            Markup.restoreRevisions(editArea.getText());
          }
        }
      );
    }
  }
}
