package org.nbp.editor.menu.input;
import org.nbp.editor.*;

public class PasteClipboard extends ClipboardAction {
  public PasteClipboard (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (editor.verifyWritableRegion()) {
      CharSequence text = getText(getClipboard());
      if (text != null) editor.getEditArea().replaceSelection(text);
    }
  }
}
