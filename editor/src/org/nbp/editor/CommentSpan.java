package org.nbp.editor;

import android.text.Editable;
import java.util.Date;

public class CommentSpan extends AuthorSpan implements DialogFinisher {
  private final Editable commentText;

  private final static String decorationPrefix = "⣏⣉";
  private final static String decorationSuffix = "⣉⣹";

  public CommentSpan (Editable text) {
    super(decorationPrefix, decorationSuffix);
    commentText = text;
  }

  public final Editable getCommentText () {
    return commentText;
  }

  @Override
  public final void finishDialog (DialogHelper helper) {
    helper.setText(R.id.comment_text, getCommentText());
    helper.setText(R.id.comment_author, getAuthor());
    helper.setText(R.id.comment_initials, getInitials());

    {
      Date timestamp = getTimestamp();
      if (timestamp != null) helper.setText(R.id.comment_timestamp, timestamp.toString());
    }
  }

  @Override
  protected void finishSpan (Editable content) {
    super.finishSpan(content);
    finishSpans(getCommentText());
  }
}
