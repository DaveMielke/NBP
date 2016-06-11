package org.nbp.editor;

public class CommentSpan extends TextSpan {
  @Override
  public final int getSpanName () {
    return R.string.span_comment;
  }

  public CommentSpan () {
    super();
  }
}
