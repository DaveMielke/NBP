package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import org.nbp.common.Spans;
import android.text.style.CharacterStyle;
import android.text.Spanned;

public class DescribeHighlighting extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = getEndpoint().getLineText();
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

          if (Spans.BOLD.isFor(span)) {
            bold = true;
          } else if (Spans.BOLD_ITALIC.isFor(span)) {
            bold = true;
            italic = true;
          } else if (Spans.ITALIC.isFor(span)) {
            italic = true;
          } else if (Spans.STRIKE.isFor(span)) {
            strike = true;
          } else if (Spans.UNDERLINE.isFor(span)) {
            underline = true;
          }
        }
      }
    }

    int[] styles = new int[4];
    int count = 0;

    if (bold) styles[count++] = R.string.DescribeHighlighting_bold;
    if (italic) styles[count++] = R.string.DescribeHighlighting_italic;
    if (strike) styles[count++] = R.string.DescribeHighlighting_strike;
    if (underline) styles[count++] = R.string.DescribeHighlighting_underline;

    if (count == 0) styles[count++] = R.string.DescribeHighlighting_none;
    StringBuilder sb = new StringBuilder();

    for (int index=0; index<count; index+=1) {
      if (sb.length() > 0) sb.append(' ');
      sb.append(ApplicationContext.getString(styles[index]));
    }

    ApplicationUtilities.message(sb.toString());
    return true;
  }

  public DescribeHighlighting (Endpoint endpoint) {
    super(endpoint, false);
  }
}
