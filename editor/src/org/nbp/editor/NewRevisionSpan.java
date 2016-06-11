package org.nbp.editor;

public class NewRevisionSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_new_revision;
  }

  @Override
  public final String getSpanIdentifier () {
    return "new";
  }

  public NewRevisionSpan () {
    super();
  }
}
