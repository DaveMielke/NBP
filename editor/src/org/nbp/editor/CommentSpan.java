package org.nbp.editor;

import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;

import android.text.Editable;
import java.util.Date;

public class CommentSpan extends ReviewSpan {
  private final Editable commentText;

  private final static String prefixDecoration = "⣏⣉";
  private final static String suffixDecoration = "⣉⣹";

  public CommentSpan (Editable text) {
    super(prefixDecoration, suffixDecoration);
    setContainsProtectedText(false);
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
  public void finishSpan (Editable content) {
    super.finishSpan(content);
    finishSpans(getCommentText());
  }
}
