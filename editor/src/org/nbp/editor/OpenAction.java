package org.nbp.editor;

import java.io.File;

public abstract class OpenAction extends EditorAction {
  protected OpenAction (EditorActivity editor) {
    super(editor);
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
