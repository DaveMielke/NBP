package org.nbp.editor;

import android.text.Editable;
import android.text.Spanned;

public abstract class Markup {
  private Markup () {
  }

  private final static <T> T[] getSpans (Spanned text, Class<T> type) {
    return text.getSpans(0, text.length(), type);
  }

  private final static RevisionSpan[] getRevisionSpans (Spanned text) {
    return getSpans(text, RevisionSpan.class);
  }

  private final static PreviewSpan[] getPreviewSpans (Spanned text) {
    return getSpans(text, PreviewSpan.class);
  }

  private final static CommentSpan[] getCommentSpans (Spanned text) {
    return getSpans(text, CommentSpan.class);
  }

  private final static void removeRevisions (Editable text, boolean preview) {
    for (RevisionSpan revision : getRevisionSpans(text)) {
      int start = text.getSpanStart(revision);
      int end = text.getSpanEnd(revision);
      text.removeSpan(revision);

      CharSequence replacement = revision.getPreviewText();
      text.replace(start, end, replacement);

      if (preview) {
        int length = replacement.length();
        int flags = (length == 0)?
                    Spanned.SPAN_POINT_POINT:
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

        text.setSpan(
          new PreviewSpan(revision), start, (start + length), flags
        );
      }
    }
  }

  public final static void previewRevisions (Editable text) {
    removeRevisions(text, true);
  }

  public final static void acceptRevisions (Editable text) {
    removeRevisions(text, false);

    for (PreviewSpan preview : getPreviewSpans(text)) {
      text.removeSpan(preview);
    }
  }

  public final static void restoreRevisions (Editable text) {
    for (PreviewSpan preview : getPreviewSpans(text)) {
      int start = text.getSpanStart(preview);
      int end = text.getSpanEnd(preview);
      text.removeSpan(preview);

      RevisionSpan revision = preview.getRevisionSpan();
      CharSequence replacement = revision.getDecoratedText();
      text.replace(start, end, replacement);

      text.setSpan(
        revision, start, (start + replacement.length()),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }

  public final static void removeComment (Editable text, CommentSpan comment) {
    int start = text.getSpanStart(comment);
    int end = text.getSpanEnd(comment);

    text.removeSpan(comment);
    text.replace(start, end, comment.getActualText());
  }

  public final static void removeComments (Editable text) {
    for (CommentSpan comment : getCommentSpans(text)) {
      removeComment(text, comment);
    }
  }

  public final static void removeMarkup (Editable text) {
    acceptRevisions(text);
    removeComments(text);
  }
}
