package org.nbp.editor.menu.input;
import org.nbp.editor.*;

public class Paste extends ClipboardAction {
  public Paste () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (editor.verifyWritableRegion()) {
      CharSequence text = getText(getClipboard(editor));
      if (text != null) editor.getEditArea().replaceSelection(text);
    }
  }
}
