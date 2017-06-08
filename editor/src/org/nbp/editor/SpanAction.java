package org.nbp.editor;

import android.text.Spanned;

public abstract class SpanAction extends EditorAction {
  protected SpanAction (EditorActivity editor) {
    super(editor);
  }

  protected final <T> T getSpan (Class<T> type, int start, int end) {
    if (end == start) end += 1;

    Spanned text = getEditArea().getText();
    if (end > text.length()) return null;

    T[] spans = text.getSpans(start, end, type);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }

  protected final <T> T getSpan (Class<T> type, int position) {
    return getSpan(type, position, position);
  }

  protected final <T> T getSpan (Class<T> type) {
    EditArea editArea = getEditArea();
    return getSpan(type, editArea.getSelectionStart(), editArea.getSelectionEnd());
  }
}
