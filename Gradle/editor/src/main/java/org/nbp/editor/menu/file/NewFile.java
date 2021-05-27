package org.nbp.editor.menu.file;
import org.nbp.editor.*;

public class NewFile extends EditorAction {
  public NewFile (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
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
