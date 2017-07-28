package org.nbp.compass;

import android.app.Activity;
import android.content.Context;

import android.content.res.Configuration;
import android.content.pm.ActivityInfo;

public enum ScreenOrientation {
  DETECT(
    Configuration.ORIENTATION_UNDEFINED,
    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
    null
  ),

  PORTRAIT(
    Configuration.ORIENTATION_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,

    new Conversions() {
      @Override
      public float getHeading (float heading) {
        return heading;
      }

      @Override
      public float getPitch (float pitch, float roll) {
        return pitch;
      }

      @Override
      public float getRoll (float pitch, float roll) {
        return roll;
      }
    }
  ),

  LANDSCAPE(
    Configuration.ORIENTATION_LANDSCAPE,
    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,

    new Conversions() {
      @Override
      public float getHeading (float heading) {
        return heading + 90f;
      }

      @Override
      public float getPitch (float pitch, float roll) {
        return -roll;
      }

      @Override
      public float getRoll (float pitch, float roll) {
        return pitch;
      }
    }
  ),
  ;

  public interface Conversions {
    public float getHeading (float heading);
    public float getPitch (float pitch, float roll);
    public float getRoll (float pitch, float roll);
  }

  private final int configurationValue;
  private final int requestValue;
  private final Conversions orientationConversions;

  private ScreenOrientation (int configuration, int request, Conversions conversions) {
    configurationValue = configuration;
    requestValue = request;
    orientationConversions = conversions;
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
      if (orientation.getConfigurationValue() == value) {
        if (orientation == DETECT) return null;
        return orientation;
      }
    }

    return PORTRAIT;
  }

  public final void setCurrentOrientation (Activity activity) {
    activity.setRequestedOrientation(getRequestValue());
  }

  public final Conversions getConversions (Context context) {
    if (orientationConversions != null) return orientationConversions;

    ScreenOrientation orientation = getCurrentOrientation(context);
    if (orientation == null) return null;

    return orientation.orientationConversions;
  }
}