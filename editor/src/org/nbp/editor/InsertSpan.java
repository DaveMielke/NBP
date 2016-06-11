package org.nbp.editor;

public class InsertSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_insert;
  }

  @Override
  public final String getSpanIdentifier () {
    return "ins";
  }

  public InsertSpan () {
    super();
  }
}
