package org.nbp.editor;

public class CommentSpan extends StructureSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_comment;
  }

  @Override
  public final String getSpanIdentifier () {
    return "com";
  }

  public CommentSpan () {
    super();
  }
}
