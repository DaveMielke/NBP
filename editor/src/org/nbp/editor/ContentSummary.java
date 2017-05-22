package org.nbp.editor;

import java.util.Map;
import java.util.HashMap;

import android.text.Spanned;

public class ContentSummary implements DialogFinisher {
  private int revisionCount = 0;
  private int insertCount = 0;
  private int deleteCount = 0;
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

    addSpanHandler(InsertSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          insertCount += 1;
        }
      }
    );

    addSpanHandler(DeleteSpan.class,
      new SpanHandler() {
        @Override
        public final void handleSpan (Object span) {
          deleteCount += 1;
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
