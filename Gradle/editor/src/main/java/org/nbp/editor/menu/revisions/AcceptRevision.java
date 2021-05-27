package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;
import org.nbp.editor.spans.RevisionSpan;

import android.content.DialogInterface;

public class AcceptRevision extends RevisionAction {
  public AcceptRevision (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    final EditArea editArea = editor.getEditArea();
    final RevisionSpan revision = getRevisionSpan();

    if (revision == null) {
      showMessage(R.string.message_original_text);
    } else if (verifyWritableText()) {
      editor.showDialog(
        R.string.menu_revisions_AcceptRevision, R.layout.revision_show,
        revision, R.string.action_accept,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            performWithoutRegionProtection(
              new Runnable() {
                @Override
                public void run () {
                  int position = Markup.acceptRevision(editArea.getText(), revision);
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
