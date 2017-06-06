package org.nbp.editor.menu.text;
import org.nbp.editor.*;

public class Cut extends ClipboardAction {
  public Cut (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    copyToClipboard(editor, true);
  }
}
