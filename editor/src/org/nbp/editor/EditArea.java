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

  public final RevisionSpan getRevisionSpan () {
    int start = getSelectionStart();
    int end = getSelectionEnd();
    if (end == start) end += 1;

    Spanned text = getText();
    if (end > text.length()) return null;

    RevisionSpan[] spans = text.getSpans(start, end, RevisionSpan.class);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }
}
