package org.nbp.editor;

import java.util.Stack;

import android.content.Context;
import android.widget.EditText;
import android.util.AttributeSet;

import android.text.Spanned;

public class EditArea extends EditText {
  public EditArea (Context context, AttributeSet attributes) {
    super(context, attributes);
  }

  private ContentHandle contentHandle = null;
  private boolean hasChanged = false;
  private boolean enforceTextProtection = true;

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

  public final void setHasChanged () {
    setHasChanged(true);
  }

  public final boolean getEnforceTextProtection () {
    return enforceTextProtection;
  }

  public final void setEnforceTextProtection (boolean yes) {
    enforceTextProtection = yes;
  }

  public final boolean containsProtectedText (Spanned text, int start, int end) {
    if (!enforceTextProtection) return false;

    EditorSpan[] spans = text.getSpans(start, end, EditorSpan.class);
    if (spans == null) return false;

    for (EditorSpan span : spans) {
      if (!span.getContainsProtectedText()) continue;
      if (text.getSpanStart(span) >= end) continue;
      if (text.getSpanEnd(span) <= start) continue;
      return true;
    }

    return false;
  }

  public final boolean containsProtectedText (int start, int end) {
    return containsProtectedText(getText(), start, end);
  }

  public final boolean containsProtectedText () {
    return containsProtectedText(getSelectionStart(), getSelectionEnd());
  }

  public final void replaceSelection (CharSequence text) {
    getText().replace(getSelectionStart(), getSelectionEnd(), text);
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

  public final void setSelection (EditorSpan span) {
    setSelection(span.getPosition(getText()));
  }

  private final <T> T getSpan (Class<T> type, int start, int end) {
    if (end == start) end += 1;

    Spanned text = getText();
    if (end > text.length()) return null;

    T[] spans = text.getSpans(start, end, type);
    if (spans == null) return null;
    if (spans.length == 0) return null;
    return spans[0];
  }

  private final <T> T getSpan (Class<T> type, int position) {
    return getSpan(type, position, position);
  }

  private final <T> T getSpan (Class<T> type) {
    return getSpan(type, getSelectionStart(), getSelectionEnd());
  }

  private final static int NO_POSITION = -1;

  private final int findNextSpan (Class<? extends EditorSpan> type) {
    int start = getSelectionEnd();
    if (start != getSelectionStart()) start -= 1;

    Spanned text = getText();
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) return NO_POSITION;

      EditorSpan span = getSpan(type, start);
      if (span != null) return span.getPosition(text);
    }
  }

  private final int findPreviousSpan (Class<? extends EditorSpan> type) {
    int start = 0;
    int end = getSelectionStart();
    Spanned text = getText();

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

  private final int findNextSpanSequence (Class<? extends EditorSpan> type) {
    int start = getSelectionEnd();
    if (start != getSelectionStart()) start -= 1;

    Spanned text = getText();
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

  private final int findPreviousSpanSequence (Class<? extends EditorSpan> type) {
    int start = 0;
    int end = getSelectionStart();

    Spanned text = getText();
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

  private final boolean moveToNextPosition (int... positions) {
    int nearest = NO_POSITION;

    for (int position : positions) {
      if (position != NO_POSITION) {
        if ((nearest == NO_POSITION) || (nearest > position)) {
          nearest = position;
        }
      }
    }

    if (nearest == NO_POSITION) return false;
    setSelection(nearest);
    return true;
  }

  private final boolean moveToPreviousPosition (int... positions) {
    int nearest = NO_POSITION;

    for (int position : positions) {
      if (position != NO_POSITION) {
        if ((nearest == NO_POSITION) || (nearest < position)) {
          nearest = position;
        }
      }
    }

    if (nearest == NO_POSITION) return false;
    setSelection(nearest);
    return true;
  }

  public final RevisionSpan getRevisionSpan () {
    return getSpan(RevisionSpan.class);
  }

  public final boolean moveToNextRevision () {
    return moveToNextPosition(
      findNextSpan(PreviewSpan.class),
      findNextSpan(RevisionSpan.class)
    );
  }

  public final boolean moveToPreviousRevision () {
    return moveToPreviousPosition(
      findPreviousSpan(PreviewSpan.class),
      findPreviousSpan(RevisionSpan.class)
    );
  }

  public final boolean moveToNextChange () {
    return moveToNextPosition(
      findNextSpanSequence(PreviewSpan.class),
      findNextSpanSequence(RevisionSpan.class)
    );
  }

  public final boolean moveToPreviousChange () {
    return moveToPreviousPosition(
      findPreviousSpanSequence(PreviewSpan.class),
      findPreviousSpanSequence(RevisionSpan.class)
    );
  }

  public final CommentSpan getCommentSpan () {
    return getSpan(CommentSpan.class);
  }

  public final boolean moveToNextComment () {
    return moveToNextPosition(
      findNextSpan(CommentSpan.class)
    );
  }

  public final boolean moveToPreviousComment () {
    return moveToPreviousPosition(
      findPreviousSpan(CommentSpan.class)
    );
  }
}
