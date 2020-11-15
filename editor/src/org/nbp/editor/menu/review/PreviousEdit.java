package org.nbp.editor.menu.review;
import org.nbp.editor.*;
import org.nbp.editor.spans.RevisionSpan;
import org.nbp.editor.spans.PreviewSpan;

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
  public void performAction () {
    if (!moveToPreviousEdit()) {
      showMessage(R.string.message_no_previous_edit);
    }
  }
}
