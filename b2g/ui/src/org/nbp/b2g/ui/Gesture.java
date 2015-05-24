package org.nbp.b2g.ui;

import android.util.Log;

public abstract class Gesture {
  private final static String LOG_TAG = Gesture.class.getName();

  private final static GestureInjector injector = Devices.pointer.get();

  private static boolean begin (int x, int y, int fingers) {
    Log.v(LOG_TAG, String.format(
      "gesture begin: [%d,%d] Fingers:%d", x, y, fingers
    ));

    if (injector == null) return true;
    return injector.gestureBegin(x, y, fingers);
  }

  private static boolean end () {
    Log.v(LOG_TAG, "gesture end");

    if (injector == null) return true;
    return injector.gestureEnd();
  }

  private static boolean move (int x, int y) {
    Log.v(LOG_TAG, String.format("gesture move: [%d,%d]", x, y));

    if (injector == null) return true;
    return injector.gestureMove(x, y);
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
      ApplicationUtilities.sleep(45);
      if (!end()) return false;
      if ((count -= 1) == 0) return true;
      ApplicationUtilities.sleep(100);
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
    double stepDistance = 10.0;
    double stepCount = Math.rint(totalDistance / stepDistance);

    double horizontalStep = horizontalDistance / stepCount;
    double verticalStep = verticalDistance / stepCount;

    if (!begin(x1, y1, fingers)) return false;
    int stepInterval = 10;

    while (true) {
      ApplicationUtilities.sleep(stepInterval);
      if (stepCount < 1.5) break;
      stepCount -= 1.0;

      horizontalPosition += horizontalStep;
      verticalPosition += verticalStep;

      int x = (int)Math.rint(horizontalPosition);
      int y = (int)Math.rint(verticalPosition);
      if (!move(x, y)) return false;
    }

    if (!move(x2, y2)) return false;
    return end();
  }

  public static boolean swipe (int x1, int y1, int x2, int y2) {
    return swipe(x1, y1, x2, y2, 1);
  }

  private Gesture () {
  }
}
