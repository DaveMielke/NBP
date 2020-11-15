package org.nbp.editor.menu.comments;
import org.nbp.editor.*;
import org.nbp.editor.spans.CommentSpan;

public class NextComment extends MoveAction {
  public NextComment (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToNextComment () {
    return moveToNextPosition(
      findNextSpan(CommentSpan.class)
    );
  }

  @Override
  public void performAction () {
    if (!moveToNextComment()) {
      showMessage(R.string.message_no_next_comment);
    }
  }
}
