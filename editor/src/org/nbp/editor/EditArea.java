package org.nbp.editor;

import java.util.Stack;

import android.content.Context;
import android.widget.EditText;
import android.util.AttributeSet;

import android.text.Spanned;
import android.text.Spannable;
import android.text.Editable;

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

  public final void setSelection (RegionSpan region) {
    String prefix = region.getDecorationPrefix();
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

  public final RevisionSpan getRevisionSpan () {
    return (RevisionSpan)getRegionSpan(RevisionSpan.class);
  }

  public final boolean moveToNextRevision () {
    return moveToNextRegion(RevisionSpan.class);
  }

  public final boolean moveToPreviousRevision () {
    return moveToPreviousRegion(RevisionSpan.class);
  }

  public final boolean moveToNextChange () {
    return moveToNextBlock(RevisionSpan.class);
  }

  public final boolean moveToPreviousChange () {
    return moveToPreviousBlock(RevisionSpan.class);
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

  private final <T> T[] getSpans (Spanned text, Class<T> type) {
    return text.getSpans(0, text.length(), type);
  }

  private final RevisionSpan[] getRevisionSpans (Spanned text) {
    return getSpans(text, RevisionSpan.class);
  }

  private final PreviewSpan[] getPreviewSpans (Spanned text) {
    return getSpans(text, PreviewSpan.class);
  }

  private final void removeRevisions (boolean preview) {
    Editable text = getText();

    for (RevisionSpan revision : getRevisionSpans(text)) {
      int start = text.getSpanStart(revision);
      int end = text.getSpanEnd(revision);
      text.removeSpan(revision);

      CharSequence replacement = revision.getPreviewText();
      text.replace(start, end, replacement);

      if (preview) {
        int length = replacement.length();
        int flags = (length == 0)?
                    Spanned.SPAN_POINT_POINT:
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

        text.setSpan(
          new PreviewSpan(revision), start, (start + length), flags
        );
      }
    }
  }

  public final void previewRevisions () {
    removeRevisions(true);
  }

  public final void acceptRevisions () {
    removeRevisions(false);
    Spannable text = getText();

    for (PreviewSpan preview : getPreviewSpans(text)) {
      text.removeSpan(preview);
    }
  }

  public final void restoreRevisions () {
    Editable text = getText();

    for (PreviewSpan preview : getPreviewSpans(text)) {
      int start = text.getSpanStart(preview);
      int end = text.getSpanEnd(preview);
      text.removeSpan(preview);

      RevisionSpan revision = preview.getRevisionSpan();
      CharSequence replacement = revision.getDecoratedText();
      text.replace(start, end, replacement);

      text.setSpan(
        revision, start, (start + replacement.length()),
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }
}
