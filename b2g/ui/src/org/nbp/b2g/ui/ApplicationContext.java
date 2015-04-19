package org.nbp.b2g.ui;

import android.util.Log;
import android.util.TypedValue;
import android.content.Context;

public abstract class ApplicationContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  private final static Object LOCK = new Object();
  private static Context applicationContext = null;

  public static Context getContext () {
    synchronized (LOCK) {
      Context context = applicationContext;
      if (context == null) Log.w(LOG_TAG, "no application context");
      return context;
    }
  }

  public static boolean setContext (Context context) {
    synchronized (LOCK) {
      if (applicationContext != null) return false;
      applicationContext = context;
    }

    Clipboard.setClipboard();
    Devices.getSpeechDevice().say(null);
    EventMonitors.startEventMonitors();
    Controls.restoreControls();
    return true;
  }

  public static String getString (int resource) {
    Context context = getContext();
    if (context == null) return null;
    return context.getResources().getString(resource);
  }

  public static int dipsToPixels (int dips) {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, getContext().getResources().getDisplayMetrics()));
  }

  private ApplicationContext () {
  }
}
