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

  public final RegionSpan getRegionSpan (Class<? extends RegionSpan> type, int start, int end) {
    if (end == start) end += 1;

    Spanned text = getText();
    if (end > text.length()) return null;

    RegionSpan[] regions = text.getSpans(start, end, type);
    if (regions == null) return null;
    if (regions.length == 0) return null;
    return regions[0];
  }

  public final RegionSpan getRegionSpan (Class<? extends RegionSpan> type, int position) {
    return getRegionSpan(type, position, position);
  }

  public final RegionSpan getRegionSpan (Class<? extends RegionSpan> type) {
    return getRegionSpan(type, getSelectionStart(), getSelectionEnd());
  }

  public final RevisionSpan getRevisionSpan () {
    return (RevisionSpan)getRegionSpan(RevisionSpan.class);
  }

  public final CommentSpan getCommentSpan () {
    return (CommentSpan)getRegionSpan(CommentSpan.class);
  }

  public final void setSelection (RegionSpan region) {
    String prefix = region.getDecorationPrefix();
    int adjustment = (prefix != null)? prefix.length(): 0;
    setSelection(getText().getSpanStart(region) + adjustment);
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

  public final boolean moveToNextRevision () {
    return moveToNextRegion(RevisionSpan.class);
  }

  public final boolean moveToNextComment () {
    return moveToNextRegion(CommentSpan.class);
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

  public final boolean moveToPreviousRevision () {
    return moveToPreviousRegion(RevisionSpan.class);
  }

  public final boolean moveToPreviousComment () {
    return moveToPreviousRegion(CommentSpan.class);
  }

  private final boolean moveToNextBlock (Class<? extends RegionSpan> type) {
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

  public final boolean moveToNextEdit () {
    return moveToNextBlock(RevisionSpan.class);
  }

  private final boolean moveToPreviousBlock (Class<? extends RegionSpan> type) {
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

  public final boolean moveToPreviousEdit () {
    return moveToPreviousBlock(RevisionSpan.class);
  }
}
