package org.nbp.common;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;

import android.content.ContentResolver;
import android.provider.Settings;

import android.accessibilityservice.AccessibilityService;

public abstract class SettingsUtilities {
  private final static String LOG_TAG = SettingsUtilities.class.getName();

  public static boolean enableAccessibilityService (Class<? extends AccessibilityService> serviceClass) {
    Context context = CommonContext.getContext();
    if (context == null) return false;
    ContentResolver resolver = context.getContentResolver();

  ADD_SERVICE:
    {
      Intent intent = new Intent(context, serviceClass);
      ComponentName component = intent.getComponent();

      String packageName = component.getPackageName();
      String longClassName = component.getClassName();
      String shortClassName = component.getShortClassName();

      String packagePrefix = packageName + '/';
      String longServiceName = packagePrefix + longClassName;
      String shortServiceName = packagePrefix + shortClassName;

      String serviceNamesKey = Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES;
      String serviceNames = Settings.Secure.getString(resolver, serviceNamesKey);
      if (serviceNames == null) serviceNames = "";

      for (String serviceName : serviceNames.split(":")) {
        if (serviceName.equals(longServiceName) || serviceName.equals(shortServiceName)) {
          Log.d(LOG_TAG, "accessibility service already enabled: " + serviceClass.getName());
          break ADD_SERVICE;
        }
      }

      if (serviceNames.length() == 0) {
        serviceNames = shortServiceName;
      } else {
        serviceNames += ":" + shortServiceName;
      }

      try {
        Settings.Secure.putString(resolver, serviceNamesKey, serviceNames);
        Log.i(LOG_TAG, "accessibility service enabled: " + serviceClass.getName());
        break ADD_SERVICE;
      } catch (SecurityException exception) {
        Log.w(LOG_TAG, ("can't enable accessibility service: " + serviceClass.getName() + ": " + exception.getMessage()));
        return false;
      }
    }

    {
      final String key = Settings.Secure.ACCESSIBILITY_ENABLED;
      final String desiredValue = "1";
      final String currentValue = Settings.Secure.getString(resolver, key);

      if (desiredValue.equals(currentValue)) {
        Log.d(LOG_TAG, "accessibility services already enabled");
      } else {
        Log.w(LOG_TAG, String.format(
          "enabling accessibility services: %s: %s -> %s",
          key, currentValue, desiredValue
        ));

        try {
          Settings.Secure.putString(resolver, key, desiredValue);
          Log.i(LOG_TAG, "accessibility services enabled");
        } catch (SecurityException exception) {
          Log.w(LOG_TAG, ("can't enable accessibility services: " + exception.getMessage()));
          return false;
        }
      }
    }

    return true;
  }

  public static String getSelectedInputMethod () {
    Context context = CommonContext.getContext();
    if (context == null) return null;

    ContentResolver resolver = context.getContentResolver();
    return Settings.Secure.getString(resolver, Settings.Secure.DEFAULT_INPUT_METHOD);
  }

  private SettingsUtilities () {
  }
}
