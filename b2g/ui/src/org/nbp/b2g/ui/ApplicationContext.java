package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import org.nbp.common.CommonContext;

import android.content.Intent;
import android.content.ComponentName;

import android.content.ContentResolver;
import android.provider.Settings;

import android.view.accessibility.AccessibilityManager;
import android.accessibilityservice.AccessibilityService;

import android.view.inputmethod.InputMethodManager;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.InputMethodInfo;

import org.nbp.b2g.ui.host.ScreenMonitor;
import org.liblouis.Louis;

public abstract class ApplicationContext extends CommonContext {
  private final static String LOG_TAG = ApplicationContext.class.getName();

  public static boolean setContext (Context context) {
    if (!CommonContext.setContext(context)) return false;

    Louis.setLogLevel(ApplicationParameters.LIBLOUIS_LOG_LEVEL);
    Louis.initialize(context);

    HostMonitor.monitorEvents(context);
    Clipboard.setClipboard(context);

    Controls.restoreCurrentValues();
    Controls.restoreSaneValues();

    Devices.speech.get().say(null);
    EventMonitors.startEventMonitors();
    enableAccessibilityService(ScreenMonitor.class);
    return true;
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

  public static boolean enableAccessibilityService (Class<? extends AccessibilityService> serviceClass) {
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

  public static InputMethodManager getInputMethodManager () {
    Object systemService = getSystemService(Context.INPUT_METHOD_SERVICE);
    if (systemService == null) return null;
    return (InputMethodManager)systemService;
  }

  public static InputMethodInfo getInputMethodInfo (Class<? extends InputMethodService> inputMethodClass) {
    String packageName = inputMethodClass.getPackage().getName();
    String className = inputMethodClass.getName();

    InputMethodManager manager = getInputMethodManager();
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
