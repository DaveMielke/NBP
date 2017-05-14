package org.nbp.editor;

public class PreviewSpan extends EditorSpan {
  private final RevisionSpan revisionSpan;

  public PreviewSpan (RevisionSpan span) {
    super();
    revisionSpan = span;
  }

  public final RevisionSpan getRevisionSpan () {
    return revisionSpan;
  }
}
