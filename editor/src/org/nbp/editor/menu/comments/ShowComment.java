package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

public class ShowComment extends CommentAction {
  public ShowComment (EditorActivity editor) {
    super(editor);
  }

  @Override
  public void performAction (EditorActivity editor) {
    CommentSpan comment = getCommentSpan();

    if (comment != null) {
      editor.showDialog(
        R.string.menu_comments_ShowComment, R.layout.comment_show, comment
      );
    } else {
      showMessage(R.string.message_uncommented_text);
    }
  }
}
