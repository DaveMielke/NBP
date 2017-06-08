package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class NextEdit extends MoveAction {
  public NextEdit (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToNextEdit () {
    return moveToNextPosition(
      findNextSpanSequence(PreviewSpan.class),
      findNextSpanSequence(RevisionSpan.class)
    );
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!moveToNextEdit()) {
      editor.showMessage(R.string.message_no_next_edit);
    }
  }
}
