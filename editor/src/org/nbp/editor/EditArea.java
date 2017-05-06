package org.nbp.editor;

import android.content.Context;
import android.widget.EditText;
import android.util.AttributeSet;
import android.text.Spanned;

public class EditArea extends EditText {
  public EditArea (Context context, AttributeSet attributes) {
    super(context, attributes);
  }

  @Override
  public void onSelectionChanged (int start, int end) {
    super.onSelectionChanged(start, end);
  }

  @Override
  public void onTextChanged (CharSequence text, int start, int before, int after) {
    super.onTextChanged(text, start, before, after);
  }

  public final boolean hasSelection () {
    return getSelectionStart() != getSelectionEnd();
  }

  public final RevisionSpan getRevisionSpan (int start, int end) {
    if (end == start) end += 1;

    Spanned text = getText();
    if (end > text.length()) return null;

    RevisionSpan[] spans = text.getSpans(start, end, RevisionSpan.class);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }

  public final RevisionSpan getRevisionSpan (int position) {
    return getRevisionSpan(position, position);
  }

  public final RevisionSpan getRevisionSpan () {
    return getRevisionSpan(getSelectionStart(), getSelectionEnd());
  }

  public final void setSelection (RevisionSpan span) {
    String prefix = span.getDecorationPrefix();
    int adjustment = (prefix != null)? prefix.length(): 0;
    setSelection(getText().getSpanStart(span) + adjustment);
  }

  public final boolean moveToNextRevision () {
    int start = getSelectionEnd();
    if (start != getSelectionStart()) start -= 1;

    Spanned text = getText();
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, RevisionSpan.class);
      if (start == end) return false;
      RevisionSpan span = getRevisionSpan(start);

      if (span != null) {
        setSelection(span);
        return true;
      }
    }
  }

  public final boolean moveToPreviousRevision () {
    int start = 0;
    int end = getSelectionStart();

    Spanned text = getText();
    RevisionSpan revision = getRevisionSpan(end);

    if (revision != null) {
      end = text.getSpanStart(revision);
      revision = null;
    }

    while (true) {
      start = text.nextSpanTransition(start, end, RevisionSpan.class);
      if (start == end) break;

      RevisionSpan span = getRevisionSpan(start);
      if (span != null) revision = span;
    }

    if (revision == null) return false;
    setSelection(revision);
    return true;
  }
}
