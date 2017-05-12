package org.nbp.editor;

import android.text.Editable;

public class CommentSpan extends AuthorSpan {
  private final Editable commentText;

  private final static String decorationPrefix = "⣏⣉";
  private final static String decorationSuffix = "⣉⣹";

  public CommentSpan (Editable text) {
    super(decorationPrefix, decorationSuffix);
    commentText = text;
  }

  public final Editable getText () {
    return commentText;
  }
}
