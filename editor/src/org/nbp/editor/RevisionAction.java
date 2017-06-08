package org.nbp.editor;

public abstract class RevisionAction extends SpanAction {
  protected RevisionAction (EditorActivity editor) {
    super(editor);
  }

  protected final RevisionSpan getRevisionSpan () {
    return getSpan(RevisionSpan.class);
  }
}
