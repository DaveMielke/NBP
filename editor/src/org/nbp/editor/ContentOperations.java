package org.nbp.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.SpannableStringBuilder;

import android.util.Log;

public abstract class ContentOperations {
  protected final void readingNotSupported () throws IOException {
    throw new IOException("reading not supported");
  }

  protected final void writingNotSupported () throws IOException {
    throw new IOException("writing not supported");
  }

  public void read (InputStream stream, SpannableStringBuilder content) throws IOException {
    readingNotSupported();
  }

  public void write (OutputStream stream, CharSequence content) throws IOException {
    writingNotSupported();
  }

  protected ContentOperations () {
  }

  protected final CharSequence getText (SpannableStringBuilder content, int start) {
    return content.subSequence(start, content.length());
  }

  protected final boolean addSpan (SpannableStringBuilder content, int start, Object span) {
    int end = content.length();
    if (end == start) return false;

    content.setSpan(span, start, end, content.SPAN_INCLUSIVE_EXCLUSIVE);
    return true;
  }

  protected final void addSpan (SpannableStringBuilder content, int start, HighlightSpans.Entry spanEntry) {
    if (start > 0) {
      CharacterStyle[] spans = content.getSpans(start-1, start, CharacterStyle.class);

      if (spans != null) {
        for (CharacterStyle span : spans) {
          if (spanEntry.isFor(span)) {
            content.setSpan(span, content.getSpanStart(span),
                            content.length(), content.getSpanFlags(span));
            return;
          }
        }
      }
    }

    addSpan(content, start, spanEntry.newInstance());
  }

  private final boolean addRevisionSpan (SpannableStringBuilder content, int start, RevisionSpan span) {
    if (!addSpan(content, start, span)) return false;
    span.decorateText(content);
    return true;
  }

  protected final RevisionSpan addInsertSpan (SpannableStringBuilder content, int start) {
    RevisionSpan span = new InsertSpan(getText(content, start));
    if (!addRevisionSpan(content, start, span)) span = null;
    return span;
  }

  protected final RevisionSpan addDeleteSpan (SpannableStringBuilder content, int start) {
    RevisionSpan span = new DeleteSpan(getText(content, start));
    if (!addRevisionSpan(content, start, span)) span = null;
    return span;
  }
}
