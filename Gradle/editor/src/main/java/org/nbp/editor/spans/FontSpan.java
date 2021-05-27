package org.nbp.editor.spans;
import org.nbp.editor.*;

import org.nbp.common.HighlightSpans;
import android.text.Spannable;

public class FontSpan extends EditorSpan {
  private final Spannable spannableContent;
  private final int spanStart;

  private final void addSpan (HighlightSpans.Entry highlight) {
    addSpan(spannableContent, spanStart, highlight);
  }

  public FontSpan (Spannable content, int start) {
    super();
    setContainsProtectedText(false);

    spannableContent = content;
    spanStart = start;

    addSpan(spannableContent, spanStart, this);
  }

  protected boolean isBold () {
    return false;
  }

  protected boolean isItalic () {
    return false;
  }

  protected boolean isUnderline () {
    return false;
  }

  protected boolean isStrikeThrough () {
    return false;
  }

  protected boolean isSuperscript () {
    return false;
  }

  protected boolean isSubscript () {
    return false;
  }

  public final void addAndroidSpans () {
    if (isBold()) {
      if (isItalic()) {
        addSpan(HighlightSpans.BOLD_ITALIC);
      } else {
        addSpan(HighlightSpans.BOLD);
      }
    } else if (isItalic()) {
      addSpan(HighlightSpans.ITALIC);
    }

    if (isUnderline()) {
      addSpan(HighlightSpans.UNDERLINE);
    }

    if (isStrikeThrough()) {
      addSpan(HighlightSpans.STRIKE);
    }

    if (isSuperscript()) {
      addSpan(HighlightSpans.SUPERSCRIPT);
    }

    if (isSubscript()) {
      addSpan(HighlightSpans.SUBSCRIPT);
    }
  }
}
