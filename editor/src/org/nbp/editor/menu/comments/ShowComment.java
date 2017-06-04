package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

public class ShowComment extends EditorAction {
  public ShowComment () {
    super();
  }

  @Override
  public void performAction (EditorActivity editor) {
    CommentSpan comment = editor.getEditArea().getCommentSpan();

    if (comment != null) {
      editor.showDialog(
        R.string.menu_comments_ShowComment, R.layout.comment_show, comment
      );
    } else {
      editor.showMessage(R.string.message_uncommented_text);
    }
  }
}
