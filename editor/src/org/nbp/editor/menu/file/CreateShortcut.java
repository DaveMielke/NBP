package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class CreateShortcut extends ShortcutAction {
  public CreateShortcut (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    performShortcutAction(editor, "com.android.launcher.action.INSTALL_SHORTCUT");
  }
}
