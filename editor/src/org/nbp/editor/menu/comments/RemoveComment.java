package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

import android.content.DialogInterface;

public class RemoveComment extends CommentAction {
  public RemoveComment (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (final EditorActivity editor) {
    final EditArea editArea = editor.getEditArea();
    final CommentSpan comment = getCommentSpan();

    if (comment == null) {
      showMessage(R.string.message_uncommented_text);
    } else if (verifyWritableText()) {
      editor.showDialog(
        R.string.menu_comments_RemoveComment, R.layout.comment_show,
        comment, R.string.action_remove,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            performWithoutRegionProtection(
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
