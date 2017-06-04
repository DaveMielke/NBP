package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import org.nbp.common.FileFinder;
import java.io.File;

public class Delete extends EditorAction {
  public Delete () {
    super();
  }

  @Override
  public void performAction (final EditorActivity editor) {
    FileFinder.Builder builder = new FileFinder
      .Builder(editor)
      .setUserTitle(R.string.menu_file_Delete)
      ;

    editor.addRootLocations(builder);

    builder.find(
      new FileFinder.FileHandler() {
        @Override
        public void handleFile (final File file) {
          if (file != null) {
            final String path = file.getAbsolutePath();

            editor.confirmAction(
              R.string.delete_question,
              path,
              new Runnable() {
                @Override
                public void run () {
                  if (!file.delete()) {
                    editor.showMessage(R.string.delete_failed, path);
                  }
                }
              }
            );
          }
        }
      }
    );
  }
}
