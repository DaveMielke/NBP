package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import java.io.File;

import org.nbp.common.PathManager;

public class ManageCurrent extends EditorAction {
  private final PathManager pathManager;

  public ManageCurrent (EditorActivity editor) {
    super(editor);
    pathManager = new PathManager(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    ContentHandle handle = getEditArea().getContentHandle();

    if (handle != null) {
      File file = handle.getFile();

      if (file != null) {
        pathManager.managePath(file);
      } else {
        showMessage(R.string.message_not_file);
      }
    } else {
      showMessage(R.string.message_manage_new);
    }
  }
}
