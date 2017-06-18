package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import org.nbp.common.FileFinder;
import java.io.File;

public class OpenFile extends FileAction {
  public OpenFile (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction () {
    getEditor().testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          findFile(
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
