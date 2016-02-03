package org.nbp.common;

import android.util.Log;

import android.content.Context;
import android.content.res.Resources;
import android.content.pm.PackageManager;

import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.graphics.Point;

import android.os.PowerManager;
import android.app.KeyguardManager;
import android.view.WindowManager;
import android.media.AudioManager;

public abstract class CommonContext {
  private final static String LOG_TAG = CommonContext.class.getName();

  private final static Object CONTEXT_LOCK = new Object();
  private static Context commonContext = null;

  public static boolean setContext (Context context) {
    synchronized (CONTEXT_LOCK) {
      if (commonContext != null) return false;
      commonContext = context.getApplicationContext();
    }

    return true;
  }

  public static Context getContext () {
    synchronized (CONTEXT_LOCK) {
      Context context = commonContext;
      if (context == null) Log.w(LOG_TAG, "no application context");
      return context;
    }
  }

  public static String getString (int resource) {
    Context context = getContext();
    if (context == null) return null;
    return context.getString(resource);
  }

  public static Resources getResources () {
    Context context = getContext();
    if (context == null) return null;
    return context.getResources();
  }

  public static String[] getStringArray (int resource) {
    Resources resources = getResources();
    if (resources == null) return null;
    return resources.getStringArray(resource);
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

  public static boolean havePermission (String permission) {
    Context context = getContext();
    if (context == null) return false;

    PackageManager pm = context.getPackageManager();
    int result = pm.checkPermission(permission, context.getPackageName());
    return result == PackageManager.PERMISSION_GRANTED;
  }

  public static Object getSystemService (String name) {
    Context context = getContext();
    if (context == null) return null;
    return context.getSystemService(name);
  }

  public static PowerManager getPowerManager () {
    Object systemService = getSystemService(Context.POWER_SERVICE);
    if (systemService == null) return null;
    return (PowerManager)systemService;
  }

  public static boolean isAwake () {
    PowerManager powerManager = getPowerManager();
    if (powerManager == null) return true;
    return powerManager.isScreenOn();
  }

  public static PowerManager.WakeLock newWakeLock (int type, String component) {
    PowerManager pm = getPowerManager();
    if (pm == null) return null;
    return pm.newWakeLock(type, ("b2g_ui-" + component));
  }

  public static KeyguardManager getKeyguardManager () {
    Object systemService = getSystemService(Context.KEYGUARD_SERVICE);
    if (systemService == null) return null;
    return (KeyguardManager)systemService;
  }

  public static boolean isKeyguardActive () {
    KeyguardManager keyguardManager = getKeyguardManager();
    if (keyguardManager == null) return false;
    return keyguardManager.inKeyguardRestrictedInputMode();
  }

  public static WindowManager getWindowManager () {
    Object systemService = getSystemService(Context.WINDOW_SERVICE);
    if (systemService == null) return null;
    return (WindowManager)systemService;
  }

  public static Point getWindowSize () {
    WindowManager windowManager = getWindowManager();
    if (windowManager == null) return null;

    Point size = new Point();
    windowManager.getDefaultDisplay().getSize(size);
    return size;
  }

  public static Point getScreenSize () {
    WindowManager windowManager = getWindowManager();
    if (windowManager == null) return null;

    Point size = new Point();
    windowManager.getDefaultDisplay().getRealSize(size);
    return size;
  }

  public static AudioManager getAudioManager () {
    Object systemService = getSystemService(Context.AUDIO_SERVICE);
    if (systemService == null) return null;
    return (AudioManager)systemService;
  }

  protected CommonContext () {
  }
}
