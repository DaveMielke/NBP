package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

import android.view.MenuItem;
import android.content.DialogInterface;

public class RemoveComment extends EditorAction {
  public RemoveComment () {
    super();
  }

  @Override
  public void performAction (final EditorActivity editor, MenuItem item) {
    final EditArea editArea = editor.getEditArea();
    final CommentSpan comment = editArea.getCommentSpan();

    if (comment == null) {
      editor.showMessage(R.string.message_uncommented_text);
    } else if (editor.verifyWritableText()) {
      editor.showDialog(
        R.string.menu_comments_RemoveComment, R.layout.comment_show,
        comment, R.string.action_remove,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            editor.performWithoutRegionProtection(
              new Runnable() {
                @Override
                public void run () {
                  int position = comment.removeSpan(editArea.getText());
                  editArea.setSelection(position);
                  editArea.setHasChanged();
                }
              }
            );
          }
        }
      );
    }
  }
}
