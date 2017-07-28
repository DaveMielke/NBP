package org.nbp.compass;

import android.app.Activity;
import android.content.Context;

import android.content.res.Configuration;
import android.content.pm.ActivityInfo;

public enum ScreenOrientation {
  CURRENT(
    Configuration.ORIENTATION_UNDEFINED,
    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,

    new Handlers() {
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

  PORTRAIT(
    Configuration.ORIENTATION_PORTRAIT,
    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,

    new Handlers() {
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

    new Handlers() {
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
        return -pitch;
      }
    }
  ),
  ;

  private interface Handlers {
    public float getHeading (float heading);
    public float getPitch (float pitch, float roll);
    public float getRoll (float pitch, float roll);
  }

  private final int configurationValue;
  private final int requestValue;
  private final Handlers orientationHandlers;

  private ScreenOrientation (int configuration, int request, Handlers handlers) {
    configurationValue = configuration;
    requestValue = request;
    orientationHandlers = handlers;
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
        if (orientation == CURRENT) return null;
        return orientation;
      }
    }

    return PORTRAIT;
  }

  public final void setCurrentOrientation (Activity activity) {
    activity.setRequestedOrientation(getRequestValue());
  }

  public final float getHeading (float heading) {
    return orientationHandlers.getHeading(heading);
  }

  public final float getPitch (float pitch, float roll) {
    return orientationHandlers.getPitch(pitch, roll);
  }

  public final float getRoll (float pitch, float roll) {
    return orientationHandlers.getRoll(pitch, roll);
  }
}
