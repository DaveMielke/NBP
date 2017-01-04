package org.nbp.common;

import android.util.Log;

public abstract class LogUtilities {
  private final static String LOG_TAG = LogUtilities.class.getName();

  public final static void log (CharSequence name, CharSequence string) {
    Log.d(LOG_TAG, String.format(
      "%s: %d/%s/",
      name, string.length(), string
    ));
  }

  protected void LogUtilities () {
  }
}
