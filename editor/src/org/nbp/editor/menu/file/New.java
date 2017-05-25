package org.nbp.editor.menu.file;
import org.nbp.editor.*;

import android.view.MenuItem;

public class New extends EditorAction {
  public New () {
    super();
  }

  @Override
  public void performAction (final EditorActivity editor, MenuItem item) {
    editor.testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          editor.setEditorContent();
          editor.checkpointFile();
        }
      }
    );
  }
}
