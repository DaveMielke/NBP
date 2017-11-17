package org.nbp.common;

import android.util.Log;
import android.text.Spanned;

public abstract class LogUtilities {
  private final static String LOG_TAG = LogUtilities.class.getName();

  protected void LogUtilities () {
  }

  private final static void log (String text) {
    Log.d(LOG_TAG, text);
  }

  private final static void log (CharSequence text) {
    log(text.toString());
  }

  public final static void logText (CharSequence label, CharSequence text) {
    log(String.format("%s: %d/%s/", label, text.length(), text));
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

    log(sb);
  }

  public final static void logSpans (CharSequence text) {
    logSpans(text, 0, text.length());
  }

  public final static void logCharacters (CharSequence label, CharSequence characters) {
    StringBuilder sb = new StringBuilder();
    sb.append(label);
    sb.append(':');
    int count = characters.length();

    for (int index=0; index<count; index+=1) {
      sb.append(String.format(" %04X", (int)characters.charAt(index)));
    }

    log(sb);
  }

  public final static void logCharacters (CharSequence label, char[] characters) {
    logCharacters(label, new String(characters));
  }
}
