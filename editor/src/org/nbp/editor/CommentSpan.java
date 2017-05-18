package org.nbp.editor;

import android.text.Editable;
import java.util.Date;

public class CommentSpan extends AuthorSpan {
  private final Editable commentText;

  private final static String decorationPrefix = "⣏⣉";
  private final static String decorationSuffix = "⣉⣹";

  public CommentSpan (Editable text) {
    super(decorationPrefix, decorationSuffix);
    setProtected(false);
    commentText = text;
  }

  public final Editable getCommentText () {
    return commentText;
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    super.finishDialog(helper);
    helper.setText(R.id.comment_text, getCommentText());
  }

  @Override
  protected void finishSpan (Editable content) {
    super.finishSpan(content);
    finishSpans(getCommentText());
  }
}
