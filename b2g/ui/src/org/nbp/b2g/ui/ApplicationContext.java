package org.nbp.b2g.ui;

import android.util.Log;

import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.graphics.Point;

import android.content.Context;
import android.content.res.Resources;
import android.content.pm.PackageManager;

import android.os.PowerManager;
import android.app.KeyguardManager;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

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
    Devices.speech.get().say(null);
    EventMonitors.startEventMonitors();
    Controls.restoreCurrentValues();
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

  public static Point getScreenSize () {
    WindowManager windowManager = getWindowManager();
    if (windowManager == null) return null;

    Point size = new Point();
    windowManager.getDefaultDisplay().getRealSize(size);
    return size;
  }

  public static AccessibilityManager getAccessibilityManager () {
    Object systemService = getSystemService(Context.ACCESSIBILITY_SERVICE);
    if (systemService == null) return null;
    return (AccessibilityManager)systemService;
  }

  public static boolean isTouchExplorationActive () {
    AccessibilityManager accessibilityManager = getAccessibilityManager();
    if (accessibilityManager == null) return false;
    return accessibilityManager.isTouchExplorationEnabled();
  }

  private ApplicationContext () {
  }
}
