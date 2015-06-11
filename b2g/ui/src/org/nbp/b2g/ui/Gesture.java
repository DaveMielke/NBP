package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Gesture {
  private final static String LOG_TAG = Gesture.class.getName();

  private final static GestureInjector injector = Devices.motion.get();
  private static long startTime = 0;

  private static void setStartTime () {
    startTime = ApplicationUtilities.getSystemClock();
  }

  private static void sleep (long duration) {
    duration -= ApplicationUtilities.getSystemClock() - startTime;
    ApplicationUtilities.sleep(duration);
    setStartTime();
  }

  private static boolean begin (int x, int y, int fingers) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format(
        "gesture begin: [%d,%d] Fingers:%d", x, y, fingers
      ));
    }

    setStartTime();
    if (injector == null) return true;
    return injector.gestureBegin(x, y, fingers);
  }

  private static boolean move (int x, int y) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format("gesture move: [%d,%d]", x, y));
    }

    if (injector == null) return true;
    return injector.gestureMove(x, y);
  }

  private static boolean end () {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, "gesture end");
    }

    if (injector == null) return true;
    return injector.gestureEnd();
  }

  private static boolean end (int x, int y) {
    if (ApplicationSettings.LOG_ACTIONS) {
      Log.v(LOG_TAG, String.format("gesture end: [%d,%d]", x, y));
    }

    if (injector == null) return true;
    return injector.gestureEnd(x, y);
  }

  public static boolean isEnabled () {
    return injector.gestureEnabled();
  }

  public static boolean tap (int x, int y, int count) {
    if (count < 1) {
      throw new IllegalArgumentException(String.format(
        "count must be greater than 0: %d", count
      ));
    }

    if (ApplicationContext.isTouchExplorationActive()) {
      count += 1;
    }

    while (true) {
      if (!begin(x, y, 1)) return false;
      sleep(ApplicationParameters.TAP_HOLD_TIME);
      if (!end()) return false;
      if ((count -= 1) == 0) return true;
      sleep(ApplicationParameters.TAP_WAIT_TIME);
    }
  }

  public static boolean tap (int x, int y) {
    return tap(x, y, 1);
  }

  public static boolean swipe (int x1, int y1, int x2, int y2, int fingers) {
    if (fingers < 1) {
      throw new IllegalArgumentException(String.format(
        "fingers must be greater than 0: %d", fingers
      ));
    }

    if (ApplicationContext.isTouchExplorationActive()) {
      fingers += 1;
    }

    double horizontalPosition = (double)x1;
    double verticalPosition = (double)y1;

    double horizontalDistance = (double)x2 - horizontalPosition;
    double verticalDistance = (double)y2 - verticalPosition;

    double totalDistance = Math.hypot(horizontalDistance, verticalDistance);
    double stepDistance = ApplicationParameters.SWIPE_STEP_DISTANCE;
    double stepCount = Math.rint(totalDistance / stepDistance);

    double horizontalStep = horizontalDistance / stepCount;
    double verticalStep = verticalDistance / stepCount;

    if (!begin(x1, y1, fingers)) return false;
    long stepInterval = ApplicationParameters.SWIPE_STEP_INTERVAL;

    while (true) {
      sleep(stepInterval);
      if (stepCount < 1.5) break;
      stepCount -= 1.0;

      horizontalPosition += horizontalStep;
      verticalPosition += verticalStep;

      int x = (int)Math.rint(horizontalPosition);
      int y = (int)Math.rint(verticalPosition);
      if (!move(x, y)) return false;
    }

    return end(x2, y2);
  }

  public static boolean swipe (int x1, int y1, int x2, int y2) {
    return swipe(x1, y1, x2, y2, 1);
  }

  private Gesture () {
  }
}
