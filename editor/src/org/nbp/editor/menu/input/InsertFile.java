package org.nbp.editor.menu.input;
import org.nbp.editor.*;

import org.nbp.common.FileFinder;
import java.io.File;
import android.text.Editable;

public class InsertFile extends FileAction {
  public InsertFile (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    if (verifyWritableRegion()) {
      findFile(
        new FileFinder.FileHandler() {
          @Override
          public void handleFile (File file) {
            if (file != null) {
              editor.readContent(
                new ContentHandle(file),
                new ContentHandler() {
                  @Override
                  public void handleContent (Editable content) {
                    editor.getEditArea().replaceSelection(content);
                  }
                }
              );
            }
          }
        }
      );
    }
  }
}
