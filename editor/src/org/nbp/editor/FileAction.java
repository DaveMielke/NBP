package org.nbp.editor;

import org.nbp.common.FileFinder;
import java.io.File;

public abstract class FileAction extends EditorAction {
  protected FileAction (EditorActivity editor) {
    super(editor);
  }

  protected final void findFile (FileFinder.FileHandler handler) {
    getEditor().findFile(false, null, handler);
  }

  protected final void loadContent (File file) {
    final EditorActivity editor = getEditor();

    editor.loadContent(
      new ContentHandle(file),
      new Runnable() {
        @Override
        public void run () {
          editor.checkpointFile();
        }
      }
    );
  }
}
