package org.nbp.editor;

public class OldRevisionSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_old_revision;
  }

  @Override
  public final String getSpanIdentifier () {
    return "old";
  }

  public OldRevisionSpan () {
    super();
  }
}
