package org.nbp.b2g.ui;

import android.util.Log;

import android.util.TypedValue;
import android.util.DisplayMetrics;
import android.graphics.Point;

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ComponentName;
import android.content.res.Resources;
import android.content.pm.PackageManager;

import android.os.PowerManager;
import android.app.KeyguardManager;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

import android.provider.Settings;
import android.accessibilityservice.AccessibilityService;

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
    enableService(ScreenMonitor.class);
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

  public static boolean enableService (Class<? extends AccessibilityService> serviceClass) {
    Context context = getContext();
    if (context == null) return false;

    Intent intent = new Intent(context, serviceClass);
    ComponentName component = intent.getComponent();
    String packageName = component.getPackageName();
    String longClassName = component.getClassName();
    String shortClassName = component.getShortClassName();

    String packagePrefix = packageName + '/';
    String longServiceName = packagePrefix + longClassName;
    String shortServiceName = packagePrefix + shortClassName;

    ContentResolver resolver = context.getContentResolver();
    String serviceNamesKey = Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES;
    String serviceNames = Settings.Secure.getString(resolver, serviceNamesKey);

    for (String serviceName : serviceNames.split(":")) {
      if (serviceName.equals(longServiceName) || serviceName.equals(shortServiceName)) {
        Log.d(LOG_TAG, "accessibility service already enabled: " + serviceClass.getName());
        return true;
      }
    }

    if (serviceNames.length() == 0) {
      serviceNames = shortServiceName;
    } else {
      serviceNames += ":" + shortServiceName;
    }

    try {
      Settings.Secure.putString(resolver, serviceNamesKey, serviceNames);
      Settings.Secure.putString(resolver, Settings.Secure.ACCESSIBILITY_ENABLED, "1");

      Log.i(LOG_TAG, "accessibility service enabled: " + serviceClass.getName());
      return true;
    } catch (SecurityException exception) {
      Log.w(LOG_TAG, "can't enable accessibility service: " + serviceClass.getName());
    }

    return false;
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
