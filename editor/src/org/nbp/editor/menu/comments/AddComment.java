package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

import org.nbp.common.CommonUtilities;
import java.util.Date;

import android.content.DialogInterface;
import android.widget.EditText;

import android.text.Editable;
import android.text.Spanned;

public class AddComment extends CommentAction {
  public AddComment (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    final String name = getAuthorName();

    if (name != null) {
      if (verifyWritableText()) {
        final EditArea editArea = editor.getEditArea();

        editor.showDialog(
          R.string.menu_comments_AddComment,
          R.layout.comment_add, R.string.action_add,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int button) {
              CommentSpan comment;

              {
                EditText view = (EditText)CommonUtilities.findView(dialog, R.id.comment_text);
                Editable text = view.getText();

                if (text.toString().trim().isEmpty()) return;
                comment = new CommentSpan(text);
                comment.setReviewTime(new Date());
                comment.setReviewerName(name);
              }

              Editable text = editArea.getText();
              int start = editArea.getSelectionStart();
              int end = editArea.getSelectionEnd();

              text.setSpan(comment, start, end, Spanned.SPAN_POINT_POINT);
              comment.finishSpan(text);
              editArea.setSelection(comment);
              editArea.setHasChanged();
            }
          }
        );
      }
    }
  }
}
