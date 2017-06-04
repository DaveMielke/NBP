package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class Copy extends ClipboardAction {
  public Copy () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    copyToClipboard(editor, false);
  }
}
