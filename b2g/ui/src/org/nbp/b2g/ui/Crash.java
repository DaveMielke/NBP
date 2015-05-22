package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Crash {
  private final static String LOG_TAG = Crash.class.getName();

  public static void handleCrash (Throwable problem, String component) {
    ApplicationUtilities.alert();

    StringBuilder sb = new StringBuilder();
    sb.append(component);
    sb.append(" crashed: ");
    sb.append(problem.getMessage());
    Log.w(LOG_TAG, sb.toString(), problem);
  }

  private Crash () {
  }
}
