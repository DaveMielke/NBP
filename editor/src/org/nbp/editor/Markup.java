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

  private final static int removeRevision (
    Editable content, RevisionSpan revision,
    CharSequence replacement, PreviewSpan preview
  ) {
    int start = content.getSpanStart(revision);
    int end = content.getSpanEnd(revision);

    content.removeSpan(revision);
    content.replace(start, end, replacement);

    if (preview != null) {
      int length = replacement.length();
      int flags = (length == 0)?
                  Spanned.SPAN_POINT_POINT:
                  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

      content.setSpan(preview, start, (start + length), flags);
    }

    return start;
  }

  private final static int applyRevision (
    Editable content, RevisionSpan revision, PreviewSpan preview
  ) {
    return removeRevision(content, revision, revision.getAcceptText(), preview);
  }

  private final static int revertRevision (
    Editable content, RevisionSpan revision, PreviewSpan preview
  ) {
    return removeRevision(content, revision, revision.getOriginalText(), preview);
  }

  public final static int acceptRevision (Editable content, RevisionSpan revision) {
    return applyRevision(content, revision, null);
  }

  public final static int rejectRevision (Editable content, RevisionSpan revision) {
    return removeRevision(content, revision, revision.getRejectText(), null);
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

  public final static boolean applyRevisions (Editable content) {
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

  public final static boolean revertRevisions (Editable content) {
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
      revertRevision(content, revisions[index], previews[index]);
    }

    return true;
  }

  public final static boolean acceptRevisions (Editable content) {
    boolean accepted = false;

    for (RevisionSpan revision : getRevisionSpans(content)) {
      acceptRevision(content, revision);
      accepted = true;
    }

    for (PreviewSpan preview : getPreviewSpans(content)) {
      content.removeSpan(preview);
      accepted = true;
    }

    return accepted;
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
