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

  protected final <T> T getSpan (Class<T> type, Spanned text, int start, int end) {
    if (end == start) end += 1;
    if (end > text.length()) return null;

    T[] spans = text.getSpans(start, end, type);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }

  protected final <T> T getSpan (Class<T> type, Spanned text, int position) {
    return getSpan(type, text, position, position);
  }

  protected final <T> T getSpan (Class<T> type, Endpoint endpoint) {
    Spanned text = toSpannedText(endpoint);
    if (text == null) return null;

    return getSpan(type, text, endpoint.getSelectionStart(), endpoint.getSelectionEnd());
  }

  protected final <T> T findNextSpan (Endpoint endpoint, Class<T> type) {
    Spanned text = toSpannedText(endpoint);
    if (text == null) return null;

    int start = endpoint.getSelectionEnd();
    if (start != endpoint.getSelectionStart()) start -= 1;
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) return null;

      T span = getSpan(type, text, start);
      if (span != null) return span;
    }
  }

  protected final <T> T findPreviousSpan (Endpoint endpoint, Class<T> type) {
    Spanned text = toSpannedText(endpoint);
    if (text == null) return null;

    int start = 0;
    int end = endpoint.getSelectionStart();

    {
      T span = getSpan(type, text, end);
      if (span != null) end = text.getSpanStart(span);
    }

    if (end == start) return null;
    T span = null;

    while (true) {
      {
        T next = getSpan(type, text, start);
        if (next != null) span = next;
      }

      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    return span;
  }

  protected final boolean moveToSpan (Endpoint endpoint, Object span) {
    Spanned text = toSpannedText(endpoint);
    if (text == null) return false;

    if (span == null) return false;
    return endpoint.setCursor(text.getSpanStart(span));
  }

  protected SpanAction (Endpoint endpoint) {
    super(endpoint, false);
  }
}
