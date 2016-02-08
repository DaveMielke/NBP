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

  private static void addColor (StringBuilder sb, int label, Integer color) {
    if (color != null) {
      startLine(sb, label);
      sb.append(Colors.getName(color));
    }
  }

  @Override
  protected final boolean performCursorKeyAction (Endpoint endpoint, int offset) {
    CharSequence text = getEndpoint().getLineText();
    Integer foregroundColor = null;
    Integer backgroundColor = null;

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
    addColor(sb, R.string.DescribeColor_foreground, foregroundColor);
    addColor(sb, R.string.DescribeColor_background, backgroundColor);

    if (sb.length() == 0) return false;
    Endpoints.setPopupEndpoint(sb.toString());
    return true;
  }

  public DescribeColor (Endpoint endpoint) {
    super(endpoint, false);
  }
}
