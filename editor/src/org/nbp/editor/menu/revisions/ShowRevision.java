package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;

public class ShowRevision extends EditorAction {
  public ShowRevision (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    RevisionSpan revision = editor.getEditArea().getRevisionSpan();

    if (revision != null) {
      editor.showDialog(
        R.string.menu_revisions_ShowRevision, R.layout.revision_show, revision
      );
    } else {
      editor.showMessage(R.string.message_original_text);
    }
  }
}
