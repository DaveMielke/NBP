package org.nbp.b2g.ui;

import android.util.Log;
import android.content.Context;

public abstract class ApplicationContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  private final static Object LOCK = new Object();
  private static Context applicationContext = null;

  public static Context get () {
    synchronized (LOCK) {
      Context context = applicationContext;
      if (context == null) Log.w(LOG_TAG, "no application context");
      return context;
    }
  }

  public static boolean set (Context context) {
    synchronized (LOCK) {
      if (applicationContext != null) return false;
      applicationContext = context;
    }

    Devices.getSpeechDevice().say(null);
    Clipboard.setClipboard();
    EventMonitors.startEventMonitors();
    return true;
  }

  private ApplicationContext () {
  }
}
