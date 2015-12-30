package org.liblouis;

import android.util.Log;
import android.content.Context;

public final class Louis {
  private final static String LOG_TAG = Louis.class.getName();

  private final static String version;
  private static native String getVersion ();

  static {
    System.loadLibrary("louis");

    version = getVersion();
    Log.i(LOG_TAG, "liblouis version: " + version);
  }

  public static void begin (Context context) {
  }

  public static void end () {
  }

  private Louis () {
  }
}
