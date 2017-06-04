package org.nbp.editor.menu.inspect;
import org.nbp.editor.*;

public class NoMarkup extends EditorAction {
  public NoMarkup () {
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
