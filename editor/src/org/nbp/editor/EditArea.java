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
    EditorSpan[] spans = text.getSpans(start, end, EditorSpan.class);

    if (enforceTextProtection) {
      if (spans != null) {
        for (EditorSpan span : spans) {
          if (!span.getContainsProtectedText()) continue;
          if (text.getSpanStart(span) >= end) continue;
          if (text.getSpanEnd(span) <= start) continue;
          return true;
        }
      }
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

  public final void setSelection (RegionSpan region) {
    String prefix = region.getPrefixDecoration();
    int adjustment = (prefix != null)? prefix.length(): 0;
    setSelection(getText().getSpanStart(region) + adjustment);
  }

  private final RegionSpan getRegionSpan (Class<? extends RegionSpan> type, int start, int end) {
    if (end == start) end += 1;

    Spanned text = getText();
    if (end > text.length()) return null;

    RegionSpan[] regions = text.getSpans(start, end, type);
    if (regions == null) return null;
    if (regions.length == 0) return null;
    return regions[0];
  }

  private final RegionSpan getRegionSpan (Class<? extends RegionSpan> type, int position) {
    return getRegionSpan(type, position, position);
  }

  private final RegionSpan getRegionSpan (Class<? extends RegionSpan> type) {
    return getRegionSpan(type, getSelectionStart(), getSelectionEnd());
  }

  private final boolean moveToNextRegion (Class<? extends RegionSpan> type) {
    int start = getSelectionEnd();
    if (start != getSelectionStart()) start -= 1;

    Spanned text = getText();
    int end = text.length();

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) return false;
      RegionSpan region = getRegionSpan(type, start);

      if (region != null) {
        setSelection(region);
        return true;
      }
    }
  }

  private final boolean moveToPreviousRegion (Class<? extends RegionSpan> type) {
    int start = 0;
    int end = getSelectionStart();
    Spanned text = getText();

    {
      RegionSpan region = getRegionSpan(type, end);
      if (region != null) end = text.getSpanStart(region);
    }

    RegionSpan region = null;

    while (true) {
      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;

      RegionSpan next = getRegionSpan(type, start);
      if (next != null) region = next;
    }

    if (region == null) return false;
    setSelection(region);
    return true;
  }

  private final boolean moveToNextGroup (Class<? extends RegionSpan> type) {
    int start = getSelectionEnd();
    if (start != getSelectionStart()) start -= 1;

    Spanned text = getText();
    int end = text.length();

    while (true) {
      RegionSpan region = getRegionSpan(type, start);
      if (region == null) break;

      start = text.nextSpanTransition(start, end, type);
      if (start == end) return false;
    }

    start = text.nextSpanTransition(start, end, type);
    if (start == end) return false;

    setSelection(getRegionSpan(type, start));
    return true;
  }

  private final boolean moveToPreviousGroup (Class<? extends RegionSpan> type) {
    int start = 0;
    int end = getSelectionStart();

    Spanned text = getText();
    Stack<RegionSpan> regions = new Stack<RegionSpan>();

    while (true) {
      regions.push(getRegionSpan(type, start));
      start = text.nextSpanTransition(start, end, type);
      if (start == end) break;
    }

    {
      RegionSpan region = getRegionSpan(type, end);
      if (region != regions.peek()) regions.push(region);
    }

    while (!regions.isEmpty()) {
      RegionSpan region = regions.pop();

      if (region == null) {
        if (regions.isEmpty()) return false;

        do {
          RegionSpan next = regions.pop();
          if (next == null) break;
          region = next;
        } while (!regions.isEmpty());

        setSelection(region);
        return true;
      }
    }

    return false;
  }

  public final RevisionSpan getRevisionSpan () {
    return (RevisionSpan)getRegionSpan(RevisionSpan.class);
  }

  public final boolean moveToNextRevision () {
    return moveToNextRegion(RevisionSpan.class);
  }

  public final boolean moveToPreviousRevision () {
    return moveToPreviousRegion(RevisionSpan.class);
  }

  public final boolean moveToNextGroup () {
    return moveToNextGroup(RevisionSpan.class);
  }

  public final boolean moveToPreviousGroup () {
    return moveToPreviousGroup(RevisionSpan.class);
  }

  public final CommentSpan getCommentSpan () {
    return (CommentSpan)getRegionSpan(CommentSpan.class);
  }

  public final boolean moveToNextComment () {
    return moveToNextRegion(CommentSpan.class);
  }

  public final boolean moveToPreviousComment () {
    return moveToPreviousRegion(CommentSpan.class);
  }
}
