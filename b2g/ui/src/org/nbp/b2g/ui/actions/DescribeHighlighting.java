package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.BackgroundColorSpan;

import android.text.Spanned;

public class DescribeHighlighting extends CursorKeyAction {
  private final static void appendString (StringBuilder sb, int string) {
    sb.append(ApplicationContext.getString(string));
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

    Integer foregroundColor = null;
    Integer backgroundColor = null;

    {
      boolean bold = false;
      boolean italic = false;
      boolean strike = false;
      boolean underline = false;

      if (text instanceof Spanned) {
        Spanned spanned = (Spanned)text;
        CharacterStyle[] spans = spanned.getSpans(offset, offset+1, CharacterStyle.class);

        if (spans != null) {
          for (CharacterStyle span : spans) {
            if (spanned.getSpanStart(span) == spanned.getSpanEnd(span)) continue;

            if (HighlightSpans.BOLD.isFor(span)) {
              bold = true;
            } else if (HighlightSpans.BOLD_ITALIC.isFor(span)) {
              bold = true;
              italic = true;
            } else if (HighlightSpans.ITALIC.isFor(span)) {
              italic = true;
            } else if (HighlightSpans.STRIKE.isFor(span)) {
              strike = true;
            } else if (HighlightSpans.UNDERLINE.isFor(span)) {
              underline = true;
            } else if (span instanceof ForegroundColorSpan) {
              foregroundColor = ((ForegroundColorSpan)span).getForegroundColor();
            } else if (span instanceof BackgroundColorSpan) {
              backgroundColor = ((BackgroundColorSpan)span).getBackgroundColor();
            }
          }
        }
      }

      int[] styleStrings = new int[4];
      int count = 0;

      if (bold) styleStrings[count++] = R.string.DescribeHighlighting_bold;
      if (italic) styleStrings[count++] = R.string.DescribeHighlighting_italic;
      if (strike) styleStrings[count++] = R.string.DescribeHighlighting_strike;
      if (underline) styleStrings[count++] = R.string.DescribeHighlighting_underline;
      if (count == 0) styleStrings[count++] = R.string.DescribeHighlighting_none;

      for (int index=0; index<count; index+=1) {
        if (sb.length() > 0) sb.append(' ');
        appendString(sb, styleStrings[index]);
      }
    }

    describeColor(sb, foregroundColor, R.string.DescribeHighlighting_foreground);
    describeColor(sb, backgroundColor, R.string.DescribeHighlighting_background);

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeHighlighting (Endpoint endpoint) {
    super(endpoint, false);
  }
}
