package org.nbp.editor;

public class CommentSpan extends AuthorSpan {
  private final static String decorationPrefix = "⣏⣉";
  private final static String decorationSuffix = "⣉⣹";

  public CommentSpan () {
    super(decorationPrefix, decorationSuffix);
  }
}
