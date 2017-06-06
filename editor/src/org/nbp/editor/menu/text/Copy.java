package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class Copy extends ClipboardAction {
  public Copy (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction () {
    copyToClipboard(false);
  }
}
