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
import android.media.AudioManager;
import android.view.accessibility.AccessibilityManager;

import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodInfo;
import android.inputmethodservice.InputMethodService;

import android.provider.Settings;
import android.accessibilityservice.AccessibilityService;

public abstract class ApplicationContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  private final static Object CONTEXT_LOCK = new Object();
  private static Context applicationContext = null;

  public static boolean setContext (Context context) {
    synchronized (CONTEXT_LOCK) {
      if (applicationContext != null) return false;
      applicationContext = context.getApplicationContext();
    }

    acquireWakeLock();
    HostMonitor.monitorEvents(context);
    Clipboard.setClipboard(context);

    Controls.restoreCurrentValues();
    Controls.restoreSaneValues();

    Devices.speech.get().say(null);
    EventMonitors.startEventMonitors();
    enableService(ScreenMonitor.class);
    return true;
  }

  public static Context getContext () {
    synchronized (CONTEXT_LOCK) {
      Context context = applicationContext;
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
    if (serviceNames == null) serviceNames = "";

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

  public static PowerManager.WakeLock newWakeLock (int type, String component) {
    PowerManager pm = getPowerManager();
    if (pm == null) return null;
    return pm.newWakeLock(type, ("b2g_ui-" + component));
  }

  private static PowerManager.WakeLock wakeLock = null;

  private static void acquireWakeLock () {
    wakeLock = newWakeLock(
      PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
      "screen"
    );

    wakeLock.acquire();
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

  public static InputMethodManager getInputMethodManager () {
    Object systemService = getSystemService(Context.INPUT_METHOD_SERVICE);
    if (systemService == null) return null;
    return (InputMethodManager)systemService;
  }

  public static InputMethodInfo getInputMethodInfo (Class<? extends InputMethodService> inputMethodClass) {
    String packageName = inputMethodClass.getPackage().getName();
    String className = inputMethodClass.getName();

    InputMethodManager manager = ApplicationContext.getInputMethodManager();
    if (manager == null) return null;

    for (InputMethodInfo info : manager.getEnabledInputMethodList()) {
      ComponentName component = info.getComponent();

      if (packageName.equals(component.getPackageName())) {
        if (className.equals(component.getClassName())) {
          return info;
        }
      }
    }

    return null;
  }

  public static String getSelectedInputMethod () {
    Context context = getContext();
    if (context == null) return null;

    ContentResolver resolver = context.getContentResolver();
    return Settings.Secure.getString(resolver, Settings.Secure.DEFAULT_INPUT_METHOD);
  }

  private ApplicationContext () {
  }
}
