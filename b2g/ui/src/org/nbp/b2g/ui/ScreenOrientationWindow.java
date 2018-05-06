package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.graphics.PixelFormat;

public class ScreenOrientationWindow extends SystemOverlayWindow {
  private final static String LOG_TAG = ScreenOrientationWindow.class.getName();

  @Override
  protected void adjustWindowParameters (WindowParameters parameters) {
    super.adjustWindowParameters(parameters);
    parameters.format = PixelFormat.RGBA_8888;
    parameters.screenOrientation = ApplicationSettings.SCREEN_ORIENTATION.getActivityOrientation();
  }

  public ScreenOrientationWindow (final Context context) {
    super(context);
  }
}
