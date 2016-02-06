package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class DescribeHighlighting extends CursorKeyAction {
  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = getEndpoint().getLineText();
    boolean bold = false;
    boolean italic = false;
    boolean underline = false;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      CharacterStyle[] spans = spanned.getSpans(offset, offset+1, CharacterStyle.class);

      if (spans != null) {
        for (CharacterStyle span : spans) {
          if (span instanceof StyleSpan) {
            switch (((StyleSpan)span).getStyle()) {
              case Typeface.BOLD:
                bold = true;
                break;

              case Typeface.ITALIC:
                italic = true;
                break;

              case Typeface.BOLD_ITALIC:
                bold = true;
                italic = true;
                break;
            }
          } else if (span instanceof UnderlineSpan) {
            underline = true;
          }
        }
      }
    }

    int[] styles = new int[3];
    int count = 0;

    if (bold) styles[count++] = R.string.DescribeHighlighting_bold;
    if (italic) styles[count++] = R.string.DescribeHighlighting_italic;
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
