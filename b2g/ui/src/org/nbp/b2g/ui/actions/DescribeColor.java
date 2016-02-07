package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.BackgroundColorSpan;

public class DescribeColor extends CursorKeyAction {
  private static void startLine (StringBuilder sb, int label) {
    if (sb.length() > 0) sb.append('\n');
    sb.append(ApplicationContext.getString(label));
    sb.append(": ");
  }

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = getEndpoint().getLineText();
    int foregroundColor = 0;
    int backgroundColor = 0;

    if (text instanceof Spanned) {
      Spanned spanned = (Spanned)text;
      CharacterStyle[] spans = spanned.getSpans(offset, offset+1, CharacterStyle.class);

      if (spans != null) {
        for (CharacterStyle span : spans) {
          if (span instanceof ForegroundColorSpan) {
            foregroundColor = ((ForegroundColorSpan)span).getForegroundColor();
          } else if (span instanceof BackgroundColorSpan) {
            backgroundColor = ((BackgroundColorSpan)span).getBackgroundColor();
          }
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    startLine(sb, R.string.DescribeColor_foreground);
    sb.append(Colors.getName(foregroundColor));
    startLine(sb, R.string.DescribeColor_background);
    sb.append(Colors.getName(backgroundColor));

    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeColor (Endpoint endpoint) {
    super(endpoint, false);
  }
}
