package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;

public class PreviousRevision extends MoveAction {
  public PreviousRevision (EditorActivity editor) {
    super(editor);
  }

  private final boolean moveToPreviousRevision () {
    return moveToPreviousPosition(
      findPreviousSpan(PreviewSpan.class),
      findPreviousSpan(RevisionSpan.class)
    );
  }

  @Override
  public void performAction () {
    if (!moveToPreviousRevision()) {
      showMessage(R.string.message_no_previous_revision);
    }
  }
}
