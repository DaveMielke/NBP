package org.nbp.editor;

public class CommentSpan extends EditorSpan {
  @Override
  public final int getName () {
    return R.string.span_comment;
  }

  public CommentSpan () {
    super();
  }
}
