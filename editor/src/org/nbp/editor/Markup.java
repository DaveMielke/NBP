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

  private final static boolean removeRevisions (Editable content, boolean preview) {
    boolean removed = false;

    for (RevisionSpan revision : getRevisionSpans(content)) {
      int start = content.getSpanStart(revision);
      int end = content.getSpanEnd(revision);
      CharSequence replacement = revision.getReplacementText();

      content.removeSpan(revision);
      content.replace(start, end, replacement);
      removed = true;

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

    return removed;
  }

  public final static boolean previewRevisions (Editable content) {
    return removeRevisions(content, true);
  }

  public final static boolean acceptRevisions (Editable content) {
    boolean removed = removeRevisions(content, false);

    for (PreviewSpan preview : getPreviewSpans(content)) {
      content.removeSpan(preview);
      removed = true;
    }

    return removed;
  }

  public final static boolean restoreRevisions (Editable content) {
    boolean restored = false;

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

      restored = true;
    }

    return restored;
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
