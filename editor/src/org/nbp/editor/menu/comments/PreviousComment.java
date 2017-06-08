package org.nbp.editor.menu.comments;
import org.nbp.editor.*;

public class PreviousComment extends MoveAction {
  public PreviousComment (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToPreviousComment () {
    return moveToPreviousPosition(
      findPreviousSpan(CommentSpan.class)
    );
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!moveToPreviousComment()) {
      editor.showMessage(R.string.message_no_previous_comment);
    }
  }
}
