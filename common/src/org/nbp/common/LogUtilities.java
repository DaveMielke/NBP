package org.nbp.common;

import android.util.Log;
import android.text.Spanned;

public abstract class LogUtilities {
  private final static String LOG_TAG = LogUtilities.class.getName();

  protected void LogUtilities () {
  }

  public final static void logText (CharSequence name, CharSequence text) {
    Log.d(LOG_TAG, String.format(
      "%s: %d/%s/",
      name, text.length(), text
    ));
  }

  public final static void logSpans (CharSequence text, int start, int end) {
    StringBuilder sb = new StringBuilder("spans:");

    sb.append(' ');
    sb.append(text.getClass().getName());

    sb.append(' ');
    sb.append(start);
    sb.append("..");
    sb.append(end);

    sb.append(' ');
    sb.append('"');
    sb.append(text.subSequence(start, end));
    sb.append('"');

    if (text instanceof Spanned) {
      Spanned content = (Spanned)text;

      Object[] spans = content.getSpans(start, end, Object.class);
      sb.append(' ');
      sb.append(spans.length);

      for (Object span : spans) {
        sb.append(' ');
        sb.append(span.getClass().getName());

        sb.append(' ');
        sb.append(content.getSpanStart(span));
        sb.append("..");
        sb.append(content.getSpanEnd(span));
      }
    }

    Log.d(LOG_TAG, sb.toString());
  }

  public final static void logSpans (CharSequence text) {
    logSpans(text, 0, text.length());
  }
}
