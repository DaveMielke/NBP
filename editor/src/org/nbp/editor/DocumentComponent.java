package org.nbp.editor;

import org.nbp.common.HighlightSpans;
import android.text.Spannable;
import android.text.style.CharacterStyle;

public abstract class DocumentComponent {
  protected DocumentComponent () {
  }

  protected static boolean addSpan (Spannable content, int start, Object span) {
    int end = content.length();
    if (end == start) return false;

    content.setSpan(span, start, end, content.SPAN_EXCLUSIVE_EXCLUSIVE);
    return true;
  }

  protected static void addSpan (Spannable content, int start, HighlightSpans.Entry highlight) {
    if (start > 0) {
      CharacterStyle[] spans = content.getSpans(start-1, start, CharacterStyle.class);

      if (spans != null) {
        for (CharacterStyle span : spans) {
          if (highlight.isFor(span)) {
            content.setSpan(span, content.getSpanStart(span),
                            content.length(), content.getSpanFlags(span));
            return;
          }
        }
      }
    }

    addSpan(content, start, highlight.newInstance());
  }
}
