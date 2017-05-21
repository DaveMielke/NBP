package org.nbp.editor;

public class PreviewSpan extends EditorSpan implements Comparable<PreviewSpan> {
  private final RevisionSpan revisionSpan;
  private final Long sortOrder;

  public PreviewSpan (RevisionSpan span, int start, int end) {
    super();
    revisionSpan = span;
    sortOrder = ((long)start << Integer.SIZE) | end;
  }

  public final RevisionSpan getRevisionSpan () {
    return revisionSpan;
  }

  @Override
  public final int compareTo (PreviewSpan reference) {
    if (reference == null) throw new NullPointerException();
    return sortOrder.compareTo(reference.sortOrder);
  }

  @Override
  public final boolean equals (Object reference) {
    if (reference == null) return false;
    return compareTo((PreviewSpan)reference) == 0;
  }
}
