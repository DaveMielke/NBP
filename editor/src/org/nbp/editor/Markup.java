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

  private final static int replaceRevision (
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
    return replaceRevision(content, revision, revision.getAcceptText(), preview);
  }

  private final static int revertRevision (
    Editable content, RevisionSpan revision, PreviewSpan preview
  ) {
    return replaceRevision(content, revision, revision.getRevertText(), preview);
  }

  public final static int acceptRevision (Editable content, RevisionSpan revision) {
    return applyRevision(content, revision, null);
  }

  public final static int rejectRevision (Editable content, RevisionSpan revision) {
    return replaceRevision(content, revision, revision.getRejectText(), null);
  }

  private interface RevisionPreviewer {
    public void previewRevision (Editable content, RevisionSpan revision, PreviewSpan preview);
  }

  private final static boolean previewRevisions (
    Editable content, RevisionPreviewer revisionPreviewer
  ) {
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
      revisionPreviewer.previewRevision(content, revisions[index], previews[index]);
    }

    return true;
  }

  public final static boolean applyRevisions (Editable content) {
    return previewRevisions(content,
      new RevisionPreviewer() {
        @Override
        public void previewRevision (Editable content, RevisionSpan revision, PreviewSpan preview) {
          applyRevision(content, revision, preview);
        }
      }
    );
  }

  public final static boolean revertRevisions (Editable content) {
    return previewRevisions(content,
      new RevisionPreviewer() {
        @Override
        public void previewRevision (Editable content, RevisionSpan revision, PreviewSpan preview) {
          revertRevision(content, revision, preview);
        }
      }
    );
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

  public final static boolean acceptRevisions (Editable content) {
    boolean accepted = false;
    restoreRevisions(content);

    for (RevisionSpan revision : getRevisionSpans(content)) {
      acceptRevision(content, revision);
      accepted = true;
    }

    return accepted;
  }

  public final static boolean removeComments (Editable content) {
    boolean removed = false;

    for (CommentSpan comment : getCommentSpans(content)) {
      comment.removeSpan(content);
      removed = true;
    }

    return removed;
  }

  public final static void removeMarkup (Editable content) {
    acceptRevisions(content);
    removeComments(content);
  }
}
