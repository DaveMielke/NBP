package org.nbp.b2g.ui;

import android.content.pm.ActivityInfo;

public enum ScreenOrientation {
  AUTOMATIC(0),
  PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
  LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
  ; // end of enumeration

  private final int activityOrientation;

  ScreenOrientation (int orientation) {
    activityOrientation = orientation;
  }

  public final int getActivityOrientation () {
    return activityOrientation;
  }
}
