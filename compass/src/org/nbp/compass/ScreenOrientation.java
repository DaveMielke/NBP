package org.nbp.compass;

import android.app.Activity;
import android.content.Context;

import android.content.res.Configuration;
import android.content.pm.ActivityInfo;

public enum ScreenOrientation {
  PORTRAIT(Configuration.ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
  LANDSCAPE(Configuration.ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
  ;

  private final int configurationValue;
  private final int requestValue;

  private ScreenOrientation (int configuration, int request) {
    configurationValue = configuration;
    requestValue = request;
  }

  public final int getConfigurationValue () {
    return configurationValue;
  }

  public final int getRequestValue () {
    return requestValue;
  }

  public final static int getConfiguredOrientation (Context context) {
    return context.getResources().getConfiguration().orientation;
  }

  public final static ScreenOrientation getCurrentOrientation (Context context) {
    int value = getConfiguredOrientation(context);

    for (ScreenOrientation orientation : ScreenOrientation.values()) {
      if (orientation.getConfigurationValue() == value) return orientation;
    }

    return PORTRAIT;
  }

  public final void setCurrentOrientation (Activity activity) {
    activity.setRequestedOrientation(getRequestValue());
  }
}
