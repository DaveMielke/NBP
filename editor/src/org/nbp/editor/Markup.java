package org.nbp.editor;

import java.util.Arrays;

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
    PreviewSpan[] spans = getSpans(content, PreviewSpan.class);
    Arrays.sort(spans);
    return spans;
  }

  private final static CommentSpan[] getCommentSpans (Spanned content) {
    return getSpans(content, CommentSpan.class);
  }

  private final static void applyRevision (
    Editable content, RevisionSpan revision, PreviewSpan preview
  ) {
    int start = content.getSpanStart(revision);
    int end = content.getSpanEnd(revision);
    CharSequence replacement = revision.getReplacementText();

    content.removeSpan(revision);
    content.replace(start, end, replacement);

    if (preview != null) {
      int length = replacement.length();
      int flags = (length == 0)?
                  Spanned.SPAN_POINT_POINT:
                  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

      content.setSpan(preview, start, (start + length), flags);
    }
  }

  public final static boolean previewRevisions (Editable content) {
    RevisionSpan[] revisions = getRevisionSpans(content);
    int count = revisions.length;
    if (count == 0) return false;

    PreviewSpan[] previews = new PreviewSpan[count];

    for (int index=0; index<count; index+=1) {
      RevisionSpan revision = revisions[index];

      previews[index] = new PreviewSpan(
        revision,
        content.getSpanStart(revision),
        content.getSpanEnd(revision)
      );
    }

    for (int index=0; index<count; index+=1) {
      applyRevision(content, revisions[index], previews[index]);
    }

    return true;
  }

  public final static boolean acceptRevisions (Editable content) {
    boolean accepted = false;

    for (RevisionSpan revision : getRevisionSpans(content)) {
      applyRevision(content, revision, null);
      accepted = true;
    }

    for (PreviewSpan preview : getPreviewSpans(content)) {
      content.removeSpan(preview);
      accepted = true;
    }

    return accepted;
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
