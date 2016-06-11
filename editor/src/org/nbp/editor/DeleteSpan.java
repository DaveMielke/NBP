package org.nbp.editor;

public class DeleteSpan extends RevisionSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_delete;
  }

  @Override
  public final String getSpanIdentifier () {
    return "del";
  }

  public DeleteSpan () {
    super();
  }
}
