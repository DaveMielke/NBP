package org.nbp.editor;

import android.text.Spanned;

public class ContentSummary implements DialogFinisher {
  private int revisionCount = 0;
  private int insertCount = 0;
  private int deleteCount = 0;
  private int commentCount = 0;

  private final void includeSpan (Object span) {
    if (span instanceof RevisionSpan) {
      revisionCount += 1;

      if (span instanceof InsertSpan) {
        insertCount += 1;
      } else if (span instanceof DeleteSpan) {
        deleteCount += 1;
      }
    } else if (span instanceof PreviewSpan) {
      PreviewSpan preview = (PreviewSpan)span;
      includeSpan(preview.getRevisionSpan());
    } else if (span instanceof CommentSpan) {
      commentCount += 1;
      CommentSpan comment = (CommentSpan)span;
      includeContent(comment.getCommentText());
    }
  }

  private final void includeContent (Spanned content) {
    Object[] spans = content.getSpans(0, content.length(), Object.class);

    if (spans != null) {
      for (Object span : spans) {
        includeSpan(span);
      }
    }
  }

  public ContentSummary (Spanned content) {
    includeContent(content);
  }

  private CharSequence contentURI = null;
  private ContentHandle contentHandle = null;
  private boolean hasChanged = false;

  public final CharSequence getContentURI () {
    return contentURI;
  }

  public final void setContentURI (CharSequence uri) {
    contentURI = uri;
  }

  public final ContentHandle getContentHandle () {
    return contentHandle;
  }

  public final void setContentHandle (ContentHandle handle) {
    contentHandle = handle;
  }

  public final boolean getHasChanged () {
    return hasChanged;
  }

  public final void setHasChanged (boolean yes) {
    hasChanged = yes;
  }

  public final int getRevisionCount () {
    return revisionCount;
  }

  public final int getInsertCount () {
    return insertCount;
  }

  public final int getDeleteCount () {
    return deleteCount;
  }

  public final int getCommentCount () {
    return commentCount;
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    helper.setText(R.id.file_summary_contentURI, getContentURI());
    helper.setValue(R.id.file_summary_hasChanged, getHasChanged());
    helper.setValue(R.id.file_summary_revisionCount, getRevisionCount());
    helper.setValue(R.id.file_summary_insertCount, getInsertCount());
    helper.setValue(R.id.file_summary_deleteCount, getDeleteCount());
    helper.setValue(R.id.file_summary_commentCount, getCommentCount());
  }
}
