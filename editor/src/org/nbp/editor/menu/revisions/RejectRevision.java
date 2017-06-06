package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;

import android.content.DialogInterface;

public class RejectRevision extends EditorAction {
  public RejectRevision (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    final EditArea editArea = editor.getEditArea();
    final RevisionSpan revision = editArea.getRevisionSpan();

    if (revision == null) {
      editor.showMessage(R.string.message_original_text);
    } else if (editor.verifyWritableText()) {
      editor.showDialog(
        R.string.menu_revisions_RejectRevision, R.layout.revision_show,
        revision, R.string.action_reject,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            editor.performWithoutRegionProtection(
              new Runnable() {
                @Override
                public void run () {
                  int position = Markup.rejectRevision(editArea.getText(), revision);
                  editArea.setSelection(position);
                  editArea.setHasChanged(true);
                }
              }
            );
          }
        }
      );
    }
  }
}
