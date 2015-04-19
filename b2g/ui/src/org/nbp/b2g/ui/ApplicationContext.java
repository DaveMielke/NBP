package org.nbp.b2g.ui;

import android.util.Log;

import android.util.TypedValue;
import android.util.DisplayMetrics;

import android.content.Context;
import android.content.res.Resources;

public abstract class ApplicationContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  private final static Object LOCK = new Object();
  private static Context applicationContext = null;

  public static boolean setContext (Context context) {
    synchronized (LOCK) {
      if (applicationContext != null) return false;
      applicationContext = context.getApplicationContext();
    }

    Clipboard.setClipboard();
    Devices.getSpeechDevice().say(null);
    EventMonitors.startEventMonitors();
    Controls.restoreControls();
    return true;
  }

  public static Context getContext () {
    synchronized (LOCK) {
      Context context = applicationContext;
      if (context == null) Log.w(LOG_TAG, "no application context");
      return context;
    }
  }

  public static Resources getResources () {
    Context context = getContext();
    if (context == null) return null;
    return context.getResources();
  }

  public static String getString (int resource) {
    Resources resources = getResources();
    if (resources == null) return null;
    return resources.getString(resource);
  }

  public static DisplayMetrics getDisplayMetrics () {
    Resources resources = getResources();
    if (resources == null) return null;
    return resources.getDisplayMetrics();
  }

  public static int dipsToPixels (int dips) {
    DisplayMetrics metrics = getDisplayMetrics();
    if (metrics == null) return dips;
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, metrics));
  }

  private ApplicationContext () {
  }
}
