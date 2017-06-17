package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import org.nbp.common.FileFinder;
import java.io.File;

public class OpenFile extends OpenAction {
  public OpenFile (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    editor.testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          editor.findFile(false, null,
            new FileFinder.FileHandler() {
              @Override
              public void handleFile (File file) {
                if (file != null) loadContent(file);
              }
            }
          );
        }
      }
    );
  }
}
