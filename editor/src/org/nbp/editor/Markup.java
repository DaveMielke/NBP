package org.nbp.editor;

import android.text.Editable;
import android.text.Spanned;

public abstract class Markup {
  private Markup () {
  }

  private final static <T> T[] getSpans (Spanned content, Class<T> type) {
    return content.getSpans(0, content.length(), type);
  }

  private final static RevisionSpan[] getRevisionSpans (Spanned content) {
    return getSpans(content, RevisionSpan.class);
  }

  private final static PreviewSpan[] getPreviewSpans (Spanned content) {
    return getSpans(content, PreviewSpan.class);
  }

  private final static CommentSpan[] getCommentSpans (Spanned content) {
    return getSpans(content, CommentSpan.class);
  }

  private final static void removeRevisions (Editable content, boolean preview) {
    for (RevisionSpan revision : getRevisionSpans(content)) {
      int start = content.getSpanStart(revision);
      int end = content.getSpanEnd(revision);
      content.removeSpan(revision);

      CharSequence replacement = revision.getReplacementText();
      content.replace(start, end, replacement);

      if (preview) {
        int length = replacement.length();
        int flags = (length == 0)?
                    Spanned.SPAN_POINT_POINT:
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

        content.setSpan(
          new PreviewSpan(revision), start, (start + length), flags
        );
      }
    }
  }

  public final static void previewRevisions (Editable content) {
    removeRevisions(content, true);
  }

  public final static void acceptRevisions (Editable content) {
    removeRevisions(content, false);

    for (PreviewSpan preview : getPreviewSpans(content)) {
      content.removeSpan(preview);
    }
  }

  public final static void restoreRevisions (Editable content) {
    for (PreviewSpan preview : getPreviewSpans(content)) {
      int start = content.getSpanStart(preview);
      int end = content.getSpanEnd(preview);
      content.removeSpan(preview);

      RevisionSpan revision = preview.getRevisionSpan();
      CharSequence replacement = revision.getDecoratedText();
      content.replace(start, end, replacement);

      content.setSpan(
        revision, start, (start + replacement.length()),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }

  public final static void removeComments (Editable content) {
    for (CommentSpan comment : getCommentSpans(content)) {
      comment.removeSpan(content);
    }
  }

  public final static void removeMarkup (Editable content) {
    acceptRevisions(content);
    removeComments(content);
  }
}
