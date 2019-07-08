package org.nbp.b2g.ui;

import android.text.Spanned;

public abstract class SpanAction extends Action {
  protected final Spanned toSpannedText (CharSequence text) {
    if (!(text instanceof Spanned)) return null;
    return (Spanned)text;
  }

  protected final Spanned toSpannedText (Endpoint endpoint) {
    return toSpannedText(endpoint.getText());
  }

  protected final <T> T getSpan (Class<? extends T> type, Spanned text, int start, int end) {
    if (end == start) end += 1;
    if (end > text.length()) return null;

    T[] spans = text.getSpans(start, end, type);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }

  protected final <T> T getSpan (Class<? extends T> type, Spanned text, int offset) {
    return getSpan(type, text, offset, offset);
  }

  protected final <T> T getSpan (Class<? extends T> type, Endpoint endpoint) {
    Spanned text = toSpannedText(endpoint);
    if (text == null) return null;

    return getSpan(type, text, endpoint.getSelectionStart(), endpoint.getSelectionEnd());
  }

  protected final static int NOT_FOUND = -1;

  protected final <T> int findNextSpan (Endpoint endpoint, Class<? extends T> type) {
    Spanned text = toSpannedText(endpoint);

    if (text != null) {
      int start = endpoint.getSelectionEnd();
      if (start != endpoint.getSelectionStart()) start -= 1;
      int end = text.length();

      while (true) {
        start = text.nextSpanTransition(start, end, type);
        if (start == end) break;

        T span = getSpan(type, text, start);
        if (span != null) return start;
      }
    }

    return NOT_FOUND;
  }

  protected final <T> int findPreviousSpan (Endpoint endpoint, Class<? extends T> type) {
    int offset = NOT_FOUND;
    Spanned text = toSpannedText(endpoint);

    if (text != null) {
      int start = 0;
      int end = endpoint.getSelectionStart();

      {
        T span = getSpan(type, text, end);
        if (span != null) end = text.getSpanStart(span);
      }

      while (start < end) {
        T next = getSpan(type, text, start);
        if (next != null) offset = start;
        start = text.nextSpanTransition(start, end, type);
      }
    }

    return offset;
  }

  protected SpanAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
