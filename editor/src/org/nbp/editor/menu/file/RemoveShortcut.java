package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class RemoveShortcut extends ShortcutAction {
  public RemoveShortcut (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    performShortcutAction(editor, "com.android.launcher.action.UNINSTALL_SHORTCUT");
  }
}
