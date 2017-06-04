package org.nbp.editor.menu.inspect;
import org.nbp.editor.*;

public class Original extends EditorAction {
  public Original () {
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
