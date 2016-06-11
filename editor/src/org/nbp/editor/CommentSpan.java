package org.nbp.editor;

public class CommentSpan extends TextSpan {
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
