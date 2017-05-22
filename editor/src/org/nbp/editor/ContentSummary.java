package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import android.text.Spanned;

public class ContentSummary implements DialogFinisher {
  private int revisionCount = 0;
  private int insertionCount = 0;
  private int deletionCount = 0;
  private int commentCount = 0;

  private interface SpanHandler {
    public void handleSpan (Object span);
  }

  private final Map<Class, SpanHandler> spanHandlers =
        new HashMap<Class, SpanHandler>();

  private final void addSpanHandler (Class type, SpanHandler handler) {
    spanHandlers.put(type, handler);
  }

  private final void addSpanHandlers () {
    addSpanHandler(RevisionSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          revisionCount += 1;
        }
      }
    );

    addSpanHandler(InsertionSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          insertionCount += 1;
        }
      }
    );

    addSpanHandler(DeletionSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          deletionCount += 1;
        }
      }
    );

    addSpanHandler(PreviewSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          PreviewSpan preview = (PreviewSpan)span;
          includeSpan(preview.getRevisionSpan());
        }
      }
    );

    addSpanHandler(CommentSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          commentCount += 1;
          CommentSpan comment = (CommentSpan)span;
          includeContent(comment.getCommentText());
        }
      }
    );
  }

  private final void includeSpan (Object span) {
    for (Class type : spanHandlers.keySet()) {
      if (type.isAssignableFrom(span.getClass())) {
        spanHandlers.get(type).handleSpan(span);
      }
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
    addSpanHandlers();
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

  public final int getInsertionCount () {
    return insertionCount;
  }

  public final int getDeletionCount () {
    return deletionCount;
  }

  public final int getCommentCount () {
    return commentCount;
  }

  @Override
  public void finishDialog (DialogHelper helper) {
    helper.setText(R.id.file_summary_contentURI, getContentURI());
    helper.setValue(R.id.file_summary_hasChanged, getHasChanged());
    helper.setValue(R.id.file_summary_revisionCount, getRevisionCount());
    helper.setValue(R.id.file_summary_insertionCount, getInsertionCount());
    helper.setValue(R.id.file_summary_deletionCount, getDeletionCount());
    helper.setValue(R.id.file_summary_commentCount, getCommentCount());
  }
}
