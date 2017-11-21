package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.util.Set;
import java.util.TreeSet;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.BackgroundColorSpan;

import android.text.Spanned;

public class DescribeHighlighting extends CursorKeyAction {
  private final static void appendString (StringBuilder sb, int string) {
    sb.append(getString(string));
  }

  private final static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    appendString(sb, label);
  }

  private final static void describeColor (StringBuilder sb, Integer color, int label) {
    if (color != null) {
      startLine(sb, label);
      sb.append(' ');
      sb.append(Colors.getName(color));
    }
  }

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = getEndpoint().getLineText();
    StringBuilder sb = new StringBuilder();

    {
      int start = endpoint.getSelectionStart();
      int end = endpoint.getSelectionEnd();

      int adjustment = endpoint.getLineStart();
      start -= adjustment;
      end -= adjustment;

      if ((start <= offset) && (offset < end)) {
        appendString(sb, R.string.DescribeHighlighting_selected);
      }
    }

    Set<Integer> spanNames = new TreeSet<Integer>();
    Integer foregroundColor = null;
    Integer backgroundColor = null;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      Object[] spans = spanned.getSpans(offset, offset+1, Object.class);

      if (spans != null) {
        for (Object span : spans) {
          if (spanned.getSpanStart(span) == spanned.getSpanEnd(span)) continue;

          if (span instanceof CharacterStyle) {
            CharacterStyle style = (CharacterStyle)span;

            if (HighlightSpans.BOLD.isFor(style)) {
              spanNames.add(R.string.DescribeHighlighting_bold);
            } else if (HighlightSpans.BOLD_ITALIC.isFor(style)) {
              spanNames.add(R.string.DescribeHighlighting_bold);
              spanNames.add(R.string.DescribeHighlighting_italic);
            } else if (HighlightSpans.ITALIC.isFor(style)) {
              spanNames.add(R.string.DescribeHighlighting_italic);
            } else if (HighlightSpans.STRIKE.isFor(style)) {
              spanNames.add(R.string.DescribeHighlighting_strike);
            } else if (HighlightSpans.UNDERLINE.isFor(style)) {
              spanNames.add(R.string.DescribeHighlighting_underline);
            } else if (style instanceof ForegroundColorSpan) {
              foregroundColor = ((ForegroundColorSpan)style).getForegroundColor();
            } else if (style instanceof BackgroundColorSpan) {
              backgroundColor = ((BackgroundColorSpan)style).getBackgroundColor();
            }
          }
        }
      }
    }

    for (Integer name : spanNames) {
      if (sb.length() > 0) sb.append(' ');
      appendString(sb, name);
    }

    describeColor(sb, foregroundColor, R.string.DescribeHighlighting_foreground);
    describeColor(sb, backgroundColor, R.string.DescribeHighlighting_background);

    if (sb.length() == 0) {
      appendString(sb, R.string.DescribeHighlighting_none);
    }

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeHighlighting (Endpoint endpoint) {
    super(endpoint, false);
  }
}
