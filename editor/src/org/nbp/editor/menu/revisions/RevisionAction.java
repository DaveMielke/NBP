package org.nbp.editor.menu.revisions;
import org.nbp.editor.*;
import org.nbp.editor.spans.RevisionSpan;

public abstract class RevisionAction extends SpanAction {
  protected RevisionAction (EditorActivity editor) {
    super(editor);
  }

  protected final RevisionSpan getRevisionSpan () {
    return getSpan(RevisionSpan.class);
  }
}
