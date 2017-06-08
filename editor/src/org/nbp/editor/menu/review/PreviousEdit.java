package org.nbp.editor.menu.review;
import org.nbp.editor.*;

public class PreviousEdit extends MoveAction {
  public PreviousEdit (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToPreviousEdit () {
    return moveToPreviousPosition(
      findPreviousSpanSequence(PreviewSpan.class),
      findPreviousSpanSequence(RevisionSpan.class)
    );
  }

  @Override
  public void performAction (EditorActivity editor) {
    if (!moveToPreviousEdit()) {
      editor.showMessage(R.string.message_no_previous_edit);
    }
  }
}
