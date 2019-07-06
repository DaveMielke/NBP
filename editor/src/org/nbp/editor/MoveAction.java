package org.nbp.editor;

import java.util.Stack;

import android.text.Spanned;

public abstract class MoveAction extends SpanAction {
  protected MoveAction (EditorActivity editor) {
    super(editor);
  }

  protected final <T> T findNextSpan (Class<T> type) {
    EditArea editArea = getEditArea();

    int start = editArea.getSelectionEnd();
    if (start != editArea.getSelectionStart()) start -= 1;

    Spanned text = editArea.getText();
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) return null;

      T span = getSpan(type, start);
      if (span != null) return span;
    }
  }

  protected final <T> T findPreviousSpan (Class<T> type) {
    EditArea editArea = getEditArea();

    int start = 0;
    int end = editArea.getSelectionStart();
    Spanned text = editArea.getText();

    {
      T span = getSpan(type, end);
      if (span != null) end = text.getSpanStart(span);
    }

    if (end == start) return null;
    T span = null;

    while (true) {
      {
        T next = getSpan(type, start);
        if (next != null) span = next;
      }

      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    return span;
  }

  protected final <T> T findNextSpanSequence (Class<T> type) {
    EditArea editArea = getEditArea();

    int start = editArea.getSelectionEnd();
    if (start != editArea.getSelectionStart()) start -= 1;

    Spanned text = editArea.getText();
    int end = text.length();

    while (true) {
      T span = getSpan(type, start);
      if (span == null) break;

      start = text.nextSpanTransition(start, end, type);
      if (start == end) return null;
    }

    start = text.nextSpanTransition(start, end, type);
    if (start == end) return null;
    return getSpan(type, start);
  }

  protected final <T> T findPreviousSpanSequence (Class<T> type) {
    EditArea editArea = getEditArea();

    int start = 0;
    int end = editArea.getSelectionStart();

    Spanned text = editArea.getText();
    Stack<T> spans = new Stack<T>();

    while (true) {
      spans.push(getSpan(type, start));
      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    {
      T span = getSpan(type, end);
      if (span != spans.peek()) spans.push(span);
    }

    while (!spans.isEmpty()) {
      T span = spans.pop();

      if (span == null) {
        if (spans.isEmpty()) return null;

        do {
          T next = spans.pop();
          if (next == null) break;
          span = next;
        } while (!spans.isEmpty());

        return span;
      }
    }

    return null;
  }

  private final static int NO_POSITION = -1;

  private final int[] getPositions (Object... spans) {
    int count = spans.length;
    int[] positions = new int[count];
    Spanned text = getEditArea().getText();

    for (int index=0; index<count; index+=1) {
      Object span = spans[index];
      int position;

      if (span == null) {
        position = NO_POSITION;
      } else if (span instanceof EditorSpan) {
        position = ((EditorSpan)span).getPosition(text);
      } else {
        position = text.getSpanStart(span);
      }

      positions[index] = position;
    }

    return positions;
  }

  private interface NearnessTester {
    public boolean isNearer (int reference, int candidate);
  }

  protected final boolean moveToNearestPosition (Object[] spans, NearnessTester nearnessTester) {
    int nearest = NO_POSITION;

    for (int position : getPositions(spans)) {
      if (position != NO_POSITION) {
        if ((nearest == NO_POSITION) || nearnessTester.isNearer(nearest, position)) {
          nearest = position;
        }
      }
    }

    if (nearest == NO_POSITION) return false;
    getEditArea().setSelection(nearest);
    return true;
  }

  protected final boolean moveToNextPosition (Object... spans) {
    return moveToNearestPosition(spans,
      new NearnessTester() {
        @Override
        public boolean isNearer (int reference, int candidate) {
          return candidate < reference;
        }
      }
    );
  }

  protected final boolean moveToPreviousPosition (Object... spans) {
    return moveToNearestPosition(spans,
      new NearnessTester() {
        @Override
        public boolean isNearer (int reference, int candidate) {
          return candidate > reference;
        }
      }
    );
  }
}
