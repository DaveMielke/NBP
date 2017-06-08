package org.nbp.editor;

import java.util.Stack;

import android.text.Spanned;

public abstract class MoveAction extends SpanAction {
  protected MoveAction (EditorActivity editor) {
    super(editor);
  }

  private final static int NO_POSITION = -1;

  protected final int findNextSpan (Class<? extends EditorSpan> type) {
    EditArea editArea = getEditArea();

    int start = editArea.getSelectionEnd();
    if (start != editArea.getSelectionStart()) start -= 1;

    Spanned text = editArea.getText();
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) return NO_POSITION;

      EditorSpan span = getSpan(type, start);
      if (span != null) return span.getPosition(text);
    }
  }

  protected final int findPreviousSpan (Class<? extends EditorSpan> type) {
    EditArea editArea = getEditArea();

    int start = 0;
    int end = editArea.getSelectionStart();
    Spanned text = editArea.getText();

    {
      EditorSpan span = getSpan(type, end);
      if (span != null) end = text.getSpanStart(span);
    }

    EditorSpan span = null;

    while (true) {
      {
        EditorSpan next = getSpan(type, start);
        if (next != null) span = next;
      }

      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    return (span != null)? span.getPosition(text): NO_POSITION;
  }

  protected final int findNextSpanSequence (Class<? extends EditorSpan> type) {
    EditArea editArea = getEditArea();

    int start = editArea.getSelectionEnd();
    if (start != editArea.getSelectionStart()) start -= 1;

    Spanned text = editArea.getText();
    int end = text.length();

    while (true) {
      EditorSpan span = getSpan(type, start);
      if (span == null) break;

      start = text.nextSpanTransition(start, end, type);
      if (start == end) return NO_POSITION;
    }

    start = text.nextSpanTransition(start, end, type);
    if (start == end) return NO_POSITION;
    return getSpan(type, start).getPosition(text);
  }

  protected final int findPreviousSpanSequence (Class<? extends EditorSpan> type) {
    EditArea editArea = getEditArea();

    int start = 0;
    int end = editArea.getSelectionStart();

    Spanned text = editArea.getText();
    Stack<EditorSpan> spans = new Stack<EditorSpan>();

    while (true) {
      spans.push(getSpan(type, start));
      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    {
      EditorSpan span = getSpan(type, end);
      if (span != spans.peek()) spans.push(span);
    }

    while (!spans.isEmpty()) {
      EditorSpan span = spans.pop();

      if (span == null) {
        if (spans.isEmpty()) return NO_POSITION;

        do {
          EditorSpan next = spans.pop();
          if (next == null) break;
          span = next;
        } while (!spans.isEmpty());

        return span.getPosition(text);
      }
    }

    return NO_POSITION;
  }

  protected final boolean moveToNextPosition (int... positions) {
    int nearest = NO_POSITION;

    for (int position : positions) {
      if (position != NO_POSITION) {
        if ((nearest == NO_POSITION) || (nearest > position)) {
          nearest = position;
        }
      }
    }

    if (nearest == NO_POSITION) return false;
    getEditArea().setSelection(nearest);
    return true;
  }

  protected final boolean moveToPreviousPosition (int... positions) {
    int nearest = NO_POSITION;

    for (int position : positions) {
      if (position != NO_POSITION) {
        if ((nearest == NO_POSITION) || (nearest < position)) {
          nearest = position;
        }
      }
    }

    if (nearest == NO_POSITION) return false;
    getEditArea().setSelection(nearest);
    return true;
  }
}
